package com.glambook.servlet.appointment;

import com.glambook.model.User;
import com.glambook.service.AppointmentService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Cancels an appointment (POST only). The record stays in the file with status CANCELLED.
 */
@WebServlet("/appointments/cancel")
public class CancelAppointmentServlet extends HttpServlet {

    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = SessionHelper.getLoggedUser(request);
        String id = request.getParameter("appointmentId");

        String error = appointmentService.cancel(id, user);
        if (error != null) {
            SessionHelper.setFlashError(request, error);
        } else {
            SessionHelper.setFlash(request, "Appointment " + id + " was cancelled.");
        }
        response.sendRedirect(request.getContextPath() + "/appointments/my");
    }
}
