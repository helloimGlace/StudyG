package com.studg.servlet;

<<<<<<< HEAD
import com.studg.dao.UserDAO;
import com.studg.model.User;

=======
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
<<<<<<< HEAD
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MysteryServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = com.studg.dao.DAOFactory.getUserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // block access if user has no plays
        HttpSession session = req.getSession(false);
=======
import java.io.IOException;

public class MysteryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // block access if user has no plays
        javax.servlet.http.HttpSession session = req.getSession(false);
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
<<<<<<< HEAD
        
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
=======
        Integer plays = (Integer) session.getAttribute("plays");
        if (plays == null) plays = 0;
        if (plays <= 0) {
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
            req.getRequestDispatcher("mystery_blocked.jsp").forward(req, resp);
            return;
        }
        // show the 16-box mystery page
        req.getRequestDispatcher("mystery.jsp").forward(req, resp);
    }
}
