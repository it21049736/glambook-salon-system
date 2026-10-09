package com.glambook.servlet.user;

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
 * GET shows the register form, POST creates a new customer account.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = Validator.clean(request.getParameter("fullName"));
        String email = Validator.clean(request.getParameter("email"));
        String phone = Validator.clean(request.getParameter("phone"));
        String password = Validator.clean(request.getParameter("password"));
        String confirmPassword = Validator.clean(request.getParameter("confirmPassword"));
        String membership = Validator.clean(request.getParameter("membership"));

        String error = userService.register(fullName, email, phone, password, confirmPassword, membership);
        if (error != null) {
            // send the user back to the form with the message and the values they typed
            request.setAttribute("error", error);
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("membership", membership);
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
            return;
        }

        // Post/Redirect/Get: redirect so refreshing the page does not register twice
        SessionHelper.setFlash(request, "Registration successful! Please log in.");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
