package com.glambook.servlet.user;

import com.glambook.service.UserService;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin page that lists and searches all users. Protected by AuthFilter (/admin/*).
 */
@WebServlet("/admin/users")
public class UserListServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = Validator.clean(request.getParameter("q"));
        request.setAttribute("users", userService.searchUsers(keyword));
        request.setAttribute("q", keyword);
        request.getRequestDispatcher("/user/user-list.jsp").forward(request, response);
    }
}
