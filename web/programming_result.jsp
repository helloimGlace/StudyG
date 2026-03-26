<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) { response.sendRedirect("login.jsp"); return; }
    Boolean parsed = (Boolean) request.getAttribute("judgeParsed");
    if (parsed == null) parsed = Boolean.FALSE;
    String respText = (String) request.getAttribute("judgeResponse");
    if (respText == null) respText = "No response from judge.";

    String status = (String) request.getAttribute("judge_status");
    String stdout = (String) request.getAttribute("judge_stdout");
    String stderr = (String) request.getAttribute("judge_stderr");
    String compile_output = (String) request.getAttribute("judge_compile_output");
    String message = (String) request.getAttribute("judge_message");
    String time = (String) request.getAttribute("judge_time");
    String memory = (String) request.getAttribute("judge_memory");
    String exit_code = (String) request.getAttribute("judge_exit_code");
    String result = (String) request.getAttribute("judge_result");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Programming Submission Result</title>
    <link rel="stylesheet" href="css/style.css" />
    <style>pre { white-space: pre-wrap; word-wrap: break-word; background:#f3f3f3; padding:10px; }</style>
</head>
<body>
    <nav>
        <a href="MainController?action=Subjects">Subjects</a> |
        <a href="MainController?action=Game">Game</a> |
        <a href="MainController?action=Shop">Shop</a> |
        <a href="inventory.jsp">Inventory</a> |
        <a href="MainController?action=Logout">Logout</a>
    </nav>
    <h2>Judge Response</h2>

<% if (parsed.booleanValue()) { %>
    <div>
        <h3>Summary</h3>
        <ul>
            <% if (status != null) { %><li><strong>Status:</strong> <%= status %></li><% } %>
            <% if (result != null) { %><li><strong>Result:</strong> <%= result %></li><% } %>
            <% if (time != null) { %><li><strong>Time:</strong> <%= time %></li><% } %>
            <% if (memory != null) { %><li><strong>Memory:</strong> <%= memory %></li><% } %>
            <% if (exit_code != null) { %><li><strong>Exit code:</strong> <%= exit_code %></li><% } %>
            <% if (message != null) { %><li><strong>Message:</strong> <%= message %></li><% } %>
        </ul>

        <% if (compile_output != null) { %>
            <h4>Compilation output</h4>
            <pre><%= compile_output %></pre>
        <% } %>
        <% if (stdout != null) { %>
            <h4>Stdout</h4>
            <pre><%= stdout %></pre>
        <% } %>
        <% if (stderr != null) { %>
            <h4>Stderr</h4>
            <pre><%= stderr %></pre>
        <% } %>
    </div>
<% } else { %>
    <pre><%= respText %></pre>
<% } %>

    <p><a href="judge">Submit another</a> | <a href="home.jsp">Home</a></p>
</body>
</html>
