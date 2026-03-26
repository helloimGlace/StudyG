<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) { response.sendRedirect("login.jsp"); return; }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>404 - Not Found</title>
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<div class="container">
    <div class="card" style="text-align: center;">
        <h2>404 - Page not found</h2>
        <p>The page you're looking for doesn't exist or is unavailable.</p>
        <a href="home.jsp" class="btn">Go Home</a>
    </div>
</div>
</body>
</html>
