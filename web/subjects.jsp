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
        subjects = Arrays.asList("Math", "Physics", "Chemistry", "History");
%>
<!DOCTYPE html>
<html>
    <head><meta charset="UTF-8"><title>Subjects</title>
        <link rel="stylesheet" href="css/style.css" />
    </head>
    <body>
        <nav>
            <a href="MainController?action=Subjects">Subjects</a> |
            <a href="MainController?action=Game">Game</a> |
            <a href="MainController?action=Shop">Shop</a> |
            <a href="inventory.jsp">Inventory</a> |
            <a href="MainController?action=Logout">Logout</a>
        </nav>
        <h2>Available Subjects</h2>
        <ul>
            <% for (String s : subjects) {%>
            <li>
                <%= s%>
                <form style="display:inline" action="MainController" method="post">
                    <input type="hidden" name="action" value="Learn" />
                    <input type="hidden" name="subject" value="<%= s%>"/>
                    <button type="submit">Learn</button>
                </form>
            </li>
            <% }%>
        </ul>
        <p><a href="home.jsp">Back</a></p>
    </body>
</html>
