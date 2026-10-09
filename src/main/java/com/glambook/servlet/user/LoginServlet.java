package com.glambook.servlet.user;

import com.glambook.model.User;
import com.glambook.service.UserService;
import com.glambook.servlet.common.SessionHelper;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * GET shows the login form, POST checks the email and password and starts a session.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        if (user != null) {
            // already logged in
            response.sendRedirect(request.getContextPath() + user.getHomePage());
            return;
        }
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = Validator.clean(request.getParameter("email"));
        String password = Validator.clean(request.getParameter("password"));

        User user = userService.login(email, password);
        if (user == null) {
            request.setAttribute("error", "Invalid email or password.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            return;
        }

        // start a fresh session and remember who is logged in
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionHelper.LOGGED_USER, user);
        SessionHelper.setFlash(request, "Welcome back, " + user.getFullName() + "!");

        // POLYMORPHISM: getHomePage() returns a different page for customers and admins
        response.sendRedirect(request.getContextPath() + user.getHomePage());
    }
}
