package com.glambook.servlet.payment;

import com.glambook.service.PaymentService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin deletes a payment record. Only VOIDED payments can be deleted.
 */
@WebServlet("/admin/payments/delete")
public class DeletePaymentServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paymentId = request.getParameter("paymentId");

        String error = paymentService.deleteVoided(paymentId);
        if (error != null) {
            SessionHelper.setFlashError(request, error);
        } else {
            SessionHelper.setFlash(request, "Voided payment " + paymentId + " was deleted.");
        }
        response.sendRedirect(request.getContextPath() + "/payments/history");
    }
}
