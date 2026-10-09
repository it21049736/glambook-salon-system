package com.glambook.servlet.payment;

import com.glambook.model.Appointment;
import com.glambook.model.Payment;
import com.glambook.model.User;
import com.glambook.service.AppointmentService;
import com.glambook.service.PaymentService;
import com.glambook.service.ServiceManager;
import com.glambook.service.StylistService;
import com.glambook.service.UserService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Shows one invoice: /payments/invoice?id=P001
 */
@WebServlet("/payments/invoice")
public class InvoiceServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        Payment payment = paymentService.getPaymentById(request.getParameter("id"));

        // customers may only open their own invoices
        if (payment == null || !paymentService.canView(user, payment)) {
            SessionHelper.setFlashError(request, "Invoice not found.");
            response.sendRedirect(request.getContextPath() + "/payments/history");
            return;
        }

        Appointment appointment = appointmentService.getAppointmentById(payment.getAppointmentId());
        request.setAttribute("payment", payment);
        request.setAttribute("appointment", appointment);
        request.setAttribute("customer", userService.getUserById(payment.getCustomerId()));
        if (appointment != null) {
            request.setAttribute("service", serviceManager.getServiceById(appointment.getServiceId()));
            request.setAttribute("stylist", stylistService.getStylistById(appointment.getStylistId()));
        }
        request.getRequestDispatcher("/payment/invoice.jsp").forward(request, response);
    }
}
