<%@ page import="java.util.*" %>
<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    // subjects list provided by servlet as request attribute
    List<String> subjects = (List<String>) request.getAttribute("subjects");
    if (subjects == null)
<<<<<<< HEAD
        subjects = Arrays.asList("Math");
=======
        subjects = Arrays.asList("Math", "Physics", "Chemistry", "History");
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
%>
<!DOCTYPE html>
<html>
    <head><meta charset="UTF-8"><title>Subjects</title>
        <link rel="stylesheet" href="css/style.css" />
    </head>
    <body>
<<<<<<< HEAD
        <nav>
            <a href="subjects">Subjects</a> | 
            <a href="game">Game</a> | 
            <a href="shop">Shop</a> | 
            <a href="inventory.jsp">Inventory</a> | 
            <a href="logout">Logout</a>
        </nav>
=======
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
        <h2>Available Subjects</h2>
        <ul>
            <% for (String s : subjects) {%>
            <li>
                <%= s%>
                <form style="display:inline" action="learn" method="post">
                    <input type="hidden" name="subject" value="<%= s%>"/>
                    <button type="submit">Learn</button>
                </form>
            </li>
            <% }%>
        </ul>
        <p><a href="home.jsp">Back</a></p>
    </body>
</html>
