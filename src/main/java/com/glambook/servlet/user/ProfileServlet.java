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

import java.io.IOException;

/**
 * GET shows the logged in user's profile, POST saves the changes.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loggedUser = SessionHelper.getLoggedUser(request);
        // read again from the file so the form shows the latest saved values
        request.setAttribute("profile", userService.getUserById(loggedUser.getUserId()));
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loggedUser = SessionHelper.getLoggedUser(request);

        String error = userService.updateProfile(
                loggedUser.getUserId(),
                Validator.clean(request.getParameter("fullName")),
                Validator.clean(request.getParameter("email")),
                Validator.clean(request.getParameter("phone")),
                Validator.clean(request.getParameter("newPassword")),
                Validator.clean(request.getParameter("membership")));

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("profile", userService.getUserById(loggedUser.getUserId()));
            request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            return;
        }

        // put the updated user into the session so the navbar shows the new name
        request.getSession().setAttribute(SessionHelper.LOGGED_USER, userService.getUserById(loggedUser.getUserId()));
        SessionHelper.setFlash(request, "Profile updated successfully.");
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
