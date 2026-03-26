<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - StudyGame</title>
    <link rel="stylesheet" href="css/style.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="container">
    <div class="card" style="max-width: 480px; margin: 2rem auto;">
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="Login" />
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" required autofocus />
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required />
            </div>
            <button type="submit" class="btn">Login</button>
            <p style="color:red; margin-top: 1rem;">${param.error}</p>
        </form>
        <p style="margin-top: 1rem;">Don't have an account? <a href="MainController?action=Register" style="color:#f97316;">Register</a></p>
    </div>
</div>
</body>
</html>
