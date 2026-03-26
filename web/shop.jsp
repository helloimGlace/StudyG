<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Integer points = (Integer) session.getAttribute("points");
    if (points == null) points = 0;
    java.util.List<com.studg.model.ShopItem> items = (java.util.List<com.studg.model.ShopItem>) request.getAttribute("items");
    if (items == null) items = java.util.Collections.emptyList();
%>
<!DOCTYPE html>
<html>
    <head><meta charset="UTF-8"><title>Shop</title>
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
        <h2>Shop</h2>
        <p>Points: <%= points %></p>

        <table border="0" cellpadding="6">
            <tr><th>Item</th><th>Price</th><th>Action</th></tr>
            <% for (com.studg.model.ShopItem it : items) { %>
                <tr>
                    <td><%= it.getDisplayName() %></td>
                    <td><%= it.getPrice() %></td>
                    <td>
                        <form action="MainController" method="post" style="display:inline">
                            <input type="hidden" name="action" value="Shop" />
                            <input type="hidden" name="itemKey" value="<%= it.getItemKey() %>" />
                            <button type="submit" <%= (points < it.getPrice()) ? "disabled" : "" %> >Buy</button>
                        </form>
                    </td>
                </tr>
            <% } %>
        </table>

        <p><a href="home.jsp">Back</a></p>
    </body>
</html>
