<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Integer plays = (Integer) session.getAttribute("plays");
    if (plays == null) plays = 0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Game</title>
    <link rel="stylesheet" href="css/style.css" />
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
    <div class="card" style="text-align: center;">
        <h2>Mystery Box</h2>
        <p>Plays available: <strong><%= plays %></strong></p>
        <% if (plays > 0) { %>
        <a class="btn" href="MainController?action=Mystery">Enter Mystery Boxes</a>
        <% } else { %>
            <div class="message">You need at least 1 play. Earn plays by studying or buy in the Shop.</div>
            <a href="MainController?action=Subjects" class="btn-secondary">Study now</a>
            <a href="MainController?action=Shop" class="btn-secondary">Go to Shop</a>
        <% } %>
        <p style="margin-top: 2rem;"><a href="home.jsp" class="btn-secondary">Back</a></p>
    </div>
</div>
</body>
</html>
