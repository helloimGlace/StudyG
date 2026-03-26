package com.studg.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JudgeController extends HttpServlet {
    public static final String ACTION = "Judge";

    private String judgeUrl = "http://127.0.0.1:8080/submit"; // default — configurable via init-param

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String cfg = config.getInitParameter("judgeUrl");
        if (cfg != null && !cfg.trim().isEmpty()) judgeUrl = cfg.trim();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // show the programming page
        req.getRequestDispatcher("programming.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // parameters: code, stdin, lang
        req.setCharacterEncoding("UTF-8");
        String code = req.getParameter("code");
        String stdin = req.getParameter("stdin");
        String lang = req.getParameter("lang");
        if (code == null) code = "";
        if (stdin == null) stdin = "";
        if (lang == null) lang = "txt";

        String responseText;
        try {
            responseText = postToJudge(code, stdin, lang);
        } catch (IOException e) {
            responseText = "Error contacting judge: " + e.getMessage();
        }

        // strip leading HTTP/status line if present (postToJudge prefixes HTTP/<status>\n)
        String rawBody = responseText;
        int idx = responseText.indexOf('\n');
        if (idx >= 0) rawBody = responseText.substring(idx + 1);

        // attempt to parse JSON-like responses to present structured output
        Map<String, String> parsed = tryParseJsonFields(rawBody);
        if (!parsed.isEmpty()) {
            req.setAttribute("judgeParsed", Boolean.TRUE);
            for (Map.Entry<String, String> e : parsed.entrySet()) {
                req.setAttribute("judge_" + e.getKey(), e.getValue());
            }
        } else {
            req.setAttribute("judgeParsed", Boolean.FALSE);
        }

        req.setAttribute("judgeResponse", rawBody);
        req.getRequestDispatcher("programming_result.jsp").forward(req, resp);
    }

    private Map<String, String> tryParseJsonFields(String body) {
        Map<String, String> out = new HashMap<>();
        if (body == null) return out;
        String b = body.trim();
        if (b.isEmpty()) return out;
        // Quick check: looks like JSON if starts with { or [
        if (!(b.startsWith("{") || b.startsWith("["))) return out;

        // keys commonly returned by judge APIs (DMOJ-like):
        String[] keys = new String[]{"status", "stdout", "stderr", "compile_output", "message", "time", "memory", "exit_code", "result"};
        for (String key : keys) {
            String val = extractJsonString(b, key);
            if (val == null) {
                String num = extractJsonNumber(b, key);
                if (num != null) out.put(key, num);
            } else {
                out.put(key, val);
            }
        }
        return out;
    }

    private String extractJsonString(String json, String key) {
        // matches "key"\s*:\s*"..." capturing inner part (handles escaped quotes loosely)
        String pattern = "\"" + Pattern.quote(key) + "\"\\s*:\\s*\"((?:\\\\\\\"|[^\\\"])*?)\"";
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher m = p.matcher(json);
        if (m.find()) {
            String raw = m.group(1);
            // unescape simple JSON escapes for common sequences
            raw = raw.replaceAll("\\\\\\\\", "\\\\"); // \\\\ -> \\\\ (preserve backslashes)
            raw = raw.replaceAll("\\\\\"", "\""); // \" -> "
            raw = raw.replaceAll("\\\\n", "\\n");
            raw = raw.replaceAll("\\\\r", "\\r");
            raw = raw.replaceAll("\\\\t", "\\t");
            return raw;
        }
        return null;
    }

    private String extractJsonNumber(String json, String key) {
        String pattern = "\"" + Pattern.quote(key) + "\"\\s*:\\s*([-+]?[0-9]*\\.?[0-9]+)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(json);
        if (m.find()) return m.group(1);
        return null;
    }

    private String postToJudge(String code, String stdin, String lang) throws IOException {
        // send application/x-www-form-urlencoded to judgeUrl with fields code, stdin, lang
        URL url = new URL(judgeUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(5000);
        con.setReadTimeout(30000);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        String body = buildFormBody(code, stdin, lang);
        try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8))) {
            w.write(body);
        }

        int status = con.getResponseCode();
        BufferedReader reader;
        if (status >= 200 && status < 400) {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(con.getErrorStream() != null ? con.getErrorStream() : con.getInputStream(), StandardCharsets.UTF_8));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/").append(status).append('\n');
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        reader.close();
        return sb.toString();
    }

    private String buildFormBody(String code, String stdin, String lang) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("code=").append(URLEncoder.encode(code, StandardCharsets.UTF_8.name()));
        sb.append("&stdin=").append(URLEncoder.encode(stdin, StandardCharsets.UTF_8.name()));
        sb.append("&lang=").append(URLEncoder.encode(lang, StandardCharsets.UTF_8.name()));
        return sb.toString();
    }
}
