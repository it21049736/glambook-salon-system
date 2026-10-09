package com.glambook.servlet.payment;

import com.glambook.model.Appointment;
import com.glambook.model.User;
import com.glambook.service.AppointmentService;
import com.glambook.service.PaymentService;
import com.glambook.service.ServiceManager;
import com.glambook.service.StylistService;
import com.glambook.servlet.common.SessionHelper;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Payment page for an appointment.
 * GET ?appointmentId=A007 shows the amount and the payment form, POST pays and creates the invoice.
 */
@WebServlet("/payments/pay")
public class PaymentServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (loadAppointment(request, response, request.getParameter("appointmentId"))) {
            request.getRequestDispatcher("/payment/pay.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        String appointmentId = Validator.clean(request.getParameter("appointmentId"));
        try {
            String paymentId = paymentService.pay(user, appointmentId,
                    Validator.clean(request.getParameter("method")),
                    Validator.clean(request.getParameter("amountTendered")),
                    Validator.clean(request.getParameter("cardHolder")),
                    Validator.clean(request.getParameter("cardNumber")),
                    Validator.clean(request.getParameter("expiry")),
                    Validator.clean(request.getParameter("cvv")));

            SessionHelper.setFlash(request, "Payment successful. Thank you!");
            response.sendRedirect(request.getContextPath() + "/payments/invoice?id=" + paymentId);
        } catch (IllegalArgumentException e) {
            // validation or processing failed: show the form again with the reason
            if (loadAppointment(request, response, appointmentId)) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/payment/pay.jsp").forward(request, response);
            }
        }
    }

    // loads the appointment for the page; redirects back if it cannot be paid
    private boolean loadAppointment(HttpServletRequest request, HttpServletResponse response, String appointmentId)
            throws IOException {
        User user = SessionHelper.getLoggedUser(request);
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null || !appointmentService.canManage(user, appointment) || !appointment.isActive()) {
            SessionHelper.setFlashError(request, "This appointment cannot be paid.");
            response.sendRedirect(request.getContextPath() + "/appointments/my");
            return false;
        }
        request.setAttribute("appointment", appointment);
        request.setAttribute("service", serviceManager.getServiceById(appointment.getServiceId()));
        request.setAttribute("stylist", stylistService.getStylistById(appointment.getStylistId()));
        return true;
    }
}
