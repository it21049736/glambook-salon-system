package com.glambook.servlet.payment;

import com.glambook.model.User;
import com.glambook.service.PaymentService;
import com.glambook.service.UserService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Payment history. Customers see their own payments; admins see all and can change status.
 */
@WebServlet("/payments/history")
public class PaymentHistoryServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        request.setAttribute("payments", paymentService.getPaymentsFor(user));
        request.setAttribute("userNames", userService.getUserNames());
        request.getRequestDispatcher("/payment/history.jsp").forward(request, response);
    }
}
