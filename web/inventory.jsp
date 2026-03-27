<%@ page import="java.util.*" %>
<%@ page session="true" %>
<%
    String username = (String) session.getAttribute("user");
    if (username == null) { response.sendRedirect("login.jsp"); return; }
    com.studg.dao.UserDAO userDAO = com.studg.dao.DAOFactory.getUserDAO();
    com.studg.model.User user = userDAO.findByUsername(username);
    Set<String> items = user != null ? user.getProfileItems() : Collections.emptySet();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inventory</title>
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
    <div class="card">
        <h2>Your Inventory</h2>
        <p>Player: <strong><%= username %></strong></p>
        <ul class="inventory-list">
            <% if (items.isEmpty()) { %>
                <li>No items yet. Visit the shop!</li>
            <% } else {
                for (String it : items) { %>
                    <li><%= it %></li>
            <%  }
            } %>
        </ul>
        <p><a href="home.jsp" class="btn-secondary">Back</a></p>
    </div>
</div>
</body>
</html>
