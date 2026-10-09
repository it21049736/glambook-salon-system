package com.glambook.servlet.user;

import com.glambook.model.User;
import com.glambook.service.UserService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Lets a customer delete their own account (POST only).
 */
@WebServlet("/profile/delete")
public class DeleteAccountServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = SessionHelper.getLoggedUser(request);

        // admin accounts are kept so the salon is never left without an admin
        if (user.canAccessAdmin()) {
            SessionHelper.setFlashError(request, "Admin accounts cannot be deleted from the profile page.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        userService.deleteUser(user.getUserId());
        request.getSession().invalidate();
        SessionHelper.setFlash(request, "Your account has been deleted. We hope to see you again!");
        response.sendRedirect(request.getContextPath() + "/");
    }
}
