package com.studg.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.studg.dao.UserDAO;
import com.studg.dao.DAOFactory;
import com.studg.model.User;

public class MainController extends HttpServlet {

    private static final String LOGIN = LoginController.ACTION;
    private static final String LOGIN_CONTROLLER = "/login";
    private static final String LOGOUT = LogoutController.ACTION;
    private static final String LOGOUT_CONTROLLER = "/logout";
    private static final String SEARCH = "Search";
    private static final String SEARCH_CONTROLLER = "/search";
    private static final String UPDATE = "Update";
    private static final String UPDATE_CONTROLLER = "/update";
    private static final String DELETE = "Delete";
    private static final String DELETE_CONTROLLER = "/delete";
    private static final String ADD = "Add";
    private static final String ADD_CONTROLLER = "/add";

    // new action/controller mappings for existing servlets
    private static final String SUBJECTS = SubjectController.ACTION;
    private static final String SUBJECTS_CONTROLLER = "/subjects";
    private static final String LEARN = LearnController.ACTION;
    private static final String LEARN_CONTROLLER = "/learn";
    private static final String TEST = TestController.ACTION;
    private static final String TEST_CONTROLLER = "/test";
    private static final String GAME = GameController.ACTION;
    private static final String GAME_CONTROLLER = "/game";
    private static final String MYSTERY = MysteryController.ACTION;
    private static final String MYSTERY_CONTROLLER = "/mystery";
    private static final String OPENBOX = OpenBoxController.ACTION;
    private static final String OPENBOX_CONTROLLER = "/openbox";
    private static final String REGISTER = RegisterController.ACTION;
    private static final String REGISTER_CONTROLLER = "/register";
    private static final String CLEARPICKED = ClearPickedController.ACTION;
    private static final String CLEARPICKED_CONTROLLER = "/clearpicked";
    private static final String SHOP = ShopController.ACTION;
    private static final String SHOP_CONTROLLER = "/shop";
    private static final String JUDGE = "Judge"; // corresponds to com.studg.controller.JudgeController.ACTION
    private static final String JUDGE_CONTROLLER = "/judge";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Refresh session user info from DB so all pages use authoritative values
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            String uname = (String) session.getAttribute("user");
            try {
                UserDAO userDao = DAOFactory.getUserDAO();
                User u = userDao.findByUsername(uname);
                if (u == null) {
                    // user no longer exists in DB -> invalidate session
                    session.invalidate();
                    response.sendRedirect("login.jsp");
                    return;
                }
                // sync session counters from DB
                session.setAttribute("points", u.getPoints());
                session.setAttribute("plays", u.getPlays());
            } catch (Exception e) {
                // DB unavailable: keep existing session values (fallback)
                log("Warning: could not refresh user from DB: " + e);
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        String url = "login.jsp";
        try {
            String action = request.getParameter("action");
            if (null != action) switch (action) {
                case LOGIN:
                    url = LOGIN_CONTROLLER;
                    break;
                case LOGOUT:
                    url = LOGOUT_CONTROLLER;
                    break;
                case SEARCH:
                    url = SEARCH_CONTROLLER;
                    break;
                case UPDATE:
                    url = UPDATE_CONTROLLER;
                    break;
                case DELETE:
                    url = DELETE_CONTROLLER;
                    break;
                case ADD:
                    url = ADD_CONTROLLER;
                    break;
                case SUBJECTS:
                    url = SUBJECTS_CONTROLLER;
                    break;
                case LEARN:
                    url = LEARN_CONTROLLER;
                    break;
                case TEST:
                    url = TEST_CONTROLLER;
                    break;
                case GAME:
                    url = GAME_CONTROLLER;
                    break;
                case MYSTERY:
                    url = MYSTERY_CONTROLLER;
                    break;
                case OPENBOX:
                    url = OPENBOX_CONTROLLER;
                    break;
                case REGISTER:
                    url = REGISTER_CONTROLLER;
                    break;
                case CLEARPICKED:
                    url = CLEARPICKED_CONTROLLER;
                    break;
                case SHOP:
                    url = SHOP_CONTROLLER;
                    break;
                case JUDGE:
                    url = JUDGE_CONTROLLER;
                    break;
                default:
                    // keep default
                    break;
            }


        } catch (Exception e) {
            log("Error at MainController: " + e);
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Main controller";
    }

}
