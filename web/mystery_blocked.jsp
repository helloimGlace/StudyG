<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) { response.sendRedirect("login.jsp"); return; }
    Integer plays = (Integer) session.getAttribute("plays"); if (plays==null) plays=0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Access Denied</title>
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<div class="container">
    <div class="card" style="text-align: center; max-width: 500px; margin: 2rem auto;">
        <h2>Access Denied</h2>
        <p>You need at least 1 play to open Mystery Boxes.</p>
        <p>Your available plays: <strong><%= plays %></strong></p>
        <p>Earn plays by learning subjects or buy them in the Shop.</p>
        <div style="display: flex; gap: 1rem; justify-content: center; margin-top: 2rem;">
            <a href="MainController?action=Subjects" class="btn">Study</a>
            <a href="MainController?action=Shop" class="btn-secondary">Shop</a>
            <a href="MainController?action=Game" class="btn-secondary">Back</a>
        </div>
    </div>
</div>
</body>
</html>

