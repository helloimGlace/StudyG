<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) { response.sendRedirect("login.jsp"); return; }
    String respText = (String) request.getAttribute("judgeResponse");
    if (respText == null) respText = "No response from judge.";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Programming Submission Result</title>
    <link rel="stylesheet" href="css/style.css" />
    <style>pre { white-space: pre-wrap; word-wrap: break-word; background:#fff7ed; padding:1rem; border-radius:16px; border:1px solid #fed7aa; }</style>
</head>
<body>
<div class="container">
    <nav class="navbar">
        <a href="MainController?action=Subjects">Subjects</a>
        <a href="MainController?action=Game">Game</a>
        <a href="MainController?action=Shop">Shop</a>
        <a href="inventory.jsp">Inventory</a>
        <a href="MainController?action=Logout">Logout</a>
    </nav>
    <div class="card">
        <h2>Judge Response</h2>
        <pre><%= respText %></pre>
        <p><a href="MainController?action=Judge" class="btn">Submit another</a> | <a href="home.jsp" class="btn-secondary">Home</a></p>
    </div>
</div>
</body>
</html>


