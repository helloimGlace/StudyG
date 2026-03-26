package com.studg.controller;

import com.studg.dao.ShopDAO;
import com.studg.dao.DAOFactory;
import com.studg.model.ShopItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ShopAdminController extends HttpServlet {
    // simple admin controller; restrict access to a user named 'alice' for now
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session==null || session.getAttribute("user")==null || !"alice".equals(session.getAttribute("user"))) {
            resp.sendRedirect("login.jsp");
            return;
        }
        ShopDAO shopDAO = DAOFactory.getShopDAO();
        List<ShopItem> items = shopDAO.getAllItems();
        req.setAttribute("items", items);
        req.getRequestDispatcher("admin_shop.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session==null || session.getAttribute("user")==null || !"alice".equals(session.getAttribute("user"))) {
            resp.sendRedirect("login.jsp");
            return;
        }
        ShopDAO shopDAO = DAOFactory.getShopDAO();
        String action = req.getParameter("op");
        if ("add".equals(action)) {
            String key = req.getParameter("itemKey");
            String name = req.getParameter("displayName");
            String priceStr = req.getParameter("price");
            int price = 0;
            try { price = Integer.parseInt(priceStr); } catch (Exception e) { price = 0; }
            if (key!=null && !key.trim().isEmpty() && name!=null && !name.trim().isEmpty() && price>0) {
                ShopItem it = new ShopItem(0, key.trim(), name.trim(), price);
                shopDAO.addItem(it);
            }
        } else if ("delete".equals(action)) {
            String key = req.getParameter("itemKey");
            if (key!=null && !key.trim().isEmpty()) shopDAO.deleteByKey(key.trim());
        }
        resp.sendRedirect("admin/shop");
    }
}

