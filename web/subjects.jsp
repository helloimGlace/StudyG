<%@ page import="java.util.*" %>
<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<String> subjects = (List<String>) request.getAttribute("subjects");
    if (subjects == null)
        subjects = Arrays.asList("Math", "Physics", "Chemistry", "History");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Subjects</title>
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
        <h2>Available Subjects</h2>
        <div class="grid">
            <% for (String s : subjects) { %>
                <div class="card" style="padding: 1rem; text-align: center;">
                    <h3><%= s %></h3>
                    <form action="MainController" method="post">
                        <input type="hidden" name="action" value="Learn" />
                        <input type="hidden" name="subject" value="<%= s %>" />
                        <button type="submit" class="btn">Learn</button>
                    </form>
                </div>
            <% } %>
        </div>
        <p><a href="home.jsp" class="btn-secondary" style="display: inline-block;">Back</a></p>
    </div>
</div>
</body>
</html>
