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
 * Admin deletes a user account from the user list.
 */
@WebServlet("/admin/users/delete")
public class AdminDeleteUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = request.getParameter("userId");
        User admin = SessionHelper.getLoggedUser(request);

        if (admin.getUserId().equals(userId)) {
            SessionHelper.setFlashError(request, "You cannot delete your own admin account.");
        } else if (userService.deleteUser(userId)) {
            SessionHelper.setFlash(request, "User " + userId + " was deleted.");
        } else {
            SessionHelper.setFlashError(request, "User not found.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
