<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) { response.sendRedirect("login.jsp"); return; }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Programming - Submit Homework</title>
    <link rel="stylesheet" href="css/style.css" />
    <style>
        textarea { width: 100%; box-sizing: border-box; font-family: monospace; }
    </style>
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
        <h2>Programming Homework (online judge)</h2>
        <p>Write your solution in the large editor below. The submission will be POSTed to a local judge server.</p>

        <form action="MainController" method="post">
            <input type="hidden" name="action" value="Judge" />
            <div class="form-group">
                <label for="lang">Language:</label>
                <select name="lang" id="lang">
                    <option value="python3">Python 3</option>
                    <option value="java">Java</option>
                    <option value="cpp">C++</option>
                    <option value="c">C</option>
                    <option value="txt">Plain text</option>
                </select>
            </div>
            <div class="form-group">
                <label for="code">Your code (very large input allowed):</label>
                <textarea id="code" name="code" rows="12" placeholder="Paste your code here..."></textarea>
            </div>
            <div class="form-group">
                <label for="stdin">Custom stdin (optional):</label>
                <textarea id="stdin" name="stdin" rows="4" placeholder="Optional input for your program"></textarea>
            </div>
            <button type="submit" class="btn">Submit to judge</button>
        </form>
        <p><a href="home.jsp" class="btn-secondary">Back</a></p>
    </div>
</div>
</body>
</html>

