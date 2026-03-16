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

        req.setAttribute("judgeResponse", responseText);
        req.getRequestDispatcher("programming_result.jsp").forward(req, resp);
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
        try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"))) {
            w.write(body);
        }

        int status = con.getResponseCode();
        BufferedReader reader;
        if (status >= 200 && status < 400) {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } else {
            reader = new BufferedReader(new InputStreamReader(con.getErrorStream() != null ? con.getErrorStream() : con.getInputStream(), "UTF-8"));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/" ).append(status).append("\n");
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        reader.close();
        return sb.toString();
    }

    private String buildFormBody(String code, String stdin, String lang) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("code=").append(URLEncoder.encode(code, "UTF-8"));
        sb.append("&stdin=").append(URLEncoder.encode(stdin, "UTF-8"));
        sb.append("&lang=").append(URLEncoder.encode(lang, "UTF-8"));
        return sb.toString();
    }
}

