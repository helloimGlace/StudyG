package com.studg.controller;

import com.studg.dao.UserDAO;
import com.studg.dao.ShopDAO;
import com.studg.dao.DAOFactory;
import com.studg.model.User;
import com.studg.model.ShopItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ShopController extends HttpServlet {
    public static final String ACTION = "Shop";

    private UserDAO userDAO;
    private ShopDAO shopDAO;

    @Override
    public void init() throws ServletException {
        userDAO = com.studg.dao.DAOFactory.getUserDAO();
        shopDAO = DAOFactory.getShopDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ShopItem> items = shopDAO.getAllItems();
        req.setAttribute("items", items);
        req.getRequestDispatcher("shop.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemKey = req.getParameter("itemKey");
        HttpSession session = req.getSession(false);
        if (session==null || session.getAttribute("user")==null) { resp.sendRedirect("login.jsp"); return; }
        String username = (String) session.getAttribute("user");
        User user = userDAO.findByUsername(username);
        if (user==null) { resp.sendRedirect("login.jsp"); return; }

        if (itemKey != null) {
            ShopItem item = shopDAO.findByKey(itemKey);
            if (item != null) {
                if (user.getPoints() >= item.getPrice()) {
                    user.setPoints(user.getPoints() - item.getPrice());
                    // special handling for known keys
                    if ("play_ticket".equals(item.getItemKey())) {
                        user.setPlays(user.getPlays() + 1);
                    } else {
                        user.addItem(item.getItemKey());
                    }
                    session.setAttribute("points", user.getPoints());
                    session.setAttribute("plays", user.getPlays());
                    userDAO.save(user);
                }
            }
        }
        resp.sendRedirect("shop");
    }
}
