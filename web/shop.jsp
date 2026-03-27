<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Integer points = (Integer) session.getAttribute("points");
    if (points == null) points = 0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shop</title>
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
        <h2>Shop</h2>
        <p>Your points: <strong><%= points %></strong></p>
        <form action="MainController" method="post" style="display: flex; gap: 1rem; justify-content: center; flex-wrap: wrap;">
            <input type="hidden" name="action" value="Shop" />
            <button type="submit" name="buy" value="play" class="btn">Buy 1 Play (100 pts)</button>
            <button type="submit" name="buy" value="item" class="btn">Buy Profile Item (200 pts)</button>
        </form>
        <p style="margin-top: 2rem;"><a href="home.jsp" class="btn-secondary">Back</a></p>
    </div>
</div>
</body>
</html>
