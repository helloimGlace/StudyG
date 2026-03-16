package com.studg.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ClearPickedController extends HttpServlet {
    public static final String ACTION = "ClearPicked";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("pickedBoxes");
        }
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
