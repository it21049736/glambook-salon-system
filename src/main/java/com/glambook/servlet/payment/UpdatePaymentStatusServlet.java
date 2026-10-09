package com.glambook.servlet.payment;

import com.glambook.service.PaymentService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin changes the status of a payment (PENDING / PAID / VOIDED).
 */
@WebServlet("/admin/payments/status")
public class UpdatePaymentStatusServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paymentId = request.getParameter("paymentId");
        String status = request.getParameter("status");

        String error = paymentService.updateStatus(paymentId, status);
        if (error != null) {
            SessionHelper.setFlashError(request, error);
        } else {
            SessionHelper.setFlash(request, "Payment " + paymentId + " is now " + status + ".");
        }
        response.sendRedirect(request.getContextPath() + "/payments/history");
    }
}
