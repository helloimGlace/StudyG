<%@ page import="java.util.*" %>
<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Home</title>
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<h1>Welcome, <%= user %></h1>
<nav>
    <a href="MainController?action=Subjects">Subjects</a> |
    <a href="MainController?action=Game">Game</a> |
    <a href="MainController?action=Shop">Shop</a> |
    <a href="inventory.jsp">Inventory</a> |
    <a href="MainController?action=Logout">Logout</a>
</nav>
</body>
</html>
