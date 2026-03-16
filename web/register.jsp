<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - StudyGame</title>
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<h2>Register</h2>
<form action="MainController" method="post">
    <input type="hidden" name="action" value="Register" />
    Username: <input type="text" name="username" required/><br/>
    Password: <input type="password" name="password" required/><br/>
    <button type="submit">Create account</button>
    <p style="color:red">${param.error}</p>
</form>
<p><a href="MainController?action=Login">Back to login</a></p>
</body>
</html>
