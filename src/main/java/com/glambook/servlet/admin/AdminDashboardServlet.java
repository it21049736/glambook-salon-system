package com.glambook.servlet.admin;

import com.glambook.model.Customer;
import com.glambook.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Admin dashboard with a quick summary of the salon.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // user statistics
        List<Customer> customers = userService.getAllCustomers();
        int premiumCount = 0;
        for (Customer customer : customers) {
            if (customer.isPremium()) {
                premiumCount++;
            }
        }
        request.setAttribute("customerCount", customers.size());
        request.setAttribute("premiumCount", premiumCount);
        request.setAttribute("adminCount", userService.getAllUsers().size() - customers.size());

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
