<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null || !"alice".equals(user)) { response.sendRedirect("login.jsp"); return; }
    java.util.List<com.studg.model.ShopItem> items = (java.util.List<com.studg.model.ShopItem>) request.getAttribute("items");
    if (items == null) items = java.util.Collections.emptyList();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Shop Items</title>
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
    <h2>Shop Administration</h2>
    <p>Logged in as admin: <b><%= user %></b></p>
    <h3>Existing items</h3>
    <table border="0" cellpadding="6">
        <tr><th>Key</th><th>Name</th><th>Price</th><th>Action</th></tr>
        <% for (com.studg.model.ShopItem it : items) { %>
            <tr>
                <td><%= it.getItemKey() %></td>
                <td><%= it.getDisplayName() %></td>
                <td><%= it.getPrice() %></td>
                <td>
                    <form action="admin/shop" method="post" style="display:inline">
                        <input type="hidden" name="op" value="delete" />
                        <input type="hidden" name="itemKey" value="<%= it.getItemKey() %>" />
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        <% } %>
    </table>

    <h3>Add new item</h3>
    <form action="admin/shop" method="post">
        <input type="hidden" name="op" value="add" />
        Key: <input type="text" name="itemKey" required /> <br/>
        Display name: <input type="text" name="displayName" required /> <br/>
        Price: <input type="number" name="price" min="1" required /> <br/>
        <button type="submit">Add Item</button>
    </form>

    <p><a href="home.jsp">Back</a></p>
</body>
</html>
