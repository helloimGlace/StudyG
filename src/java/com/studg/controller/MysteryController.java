package com.studg.controller;

import com.studg.dao.UserDAO;
import com.studg.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MysteryController extends HttpServlet {
    public static final String ACTION = "Mystery";

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = com.studg.dao.DAOFactory.getUserDAO();
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // block access if user has no plays
        javax.servlet.http.HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        
        // Always fetch fresh user from database to sync plays count
        String username = (String) session.getAttribute("user");
        User user = userDAO.findByUsername(username);
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        
        // Update session with latest values from database
        session.setAttribute("points", user.getPoints());
        session.setAttribute("plays", user.getPlays());
        
        if (user.getPlays() <= 0) {
            Integer plays = (Integer) session.getAttribute("plays");
            if (plays == null) plays = 0;
            if (plays <= 0) {
                req.getRequestDispatcher("mystery_blocked.jsp").forward(req, resp);
                return;
            }
            // show the 16-box mystery page
            req.getRequestDispatcher("mystery.jsp").forward(req, resp);
        }
    }
}