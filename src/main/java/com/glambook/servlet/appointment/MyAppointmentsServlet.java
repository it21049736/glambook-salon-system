package com.glambook.servlet.appointment;

import com.glambook.model.Appointment;
import com.glambook.model.User;
import com.glambook.service.AppointmentService;
import com.glambook.service.ServiceManager;
import com.glambook.service.StylistService;
import com.glambook.service.UserService;
import com.glambook.servlet.common.SessionHelper;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lists appointments. Customers see their own, admins see all (optional ?status= filter).
 */
@WebServlet("/appointments/my")
public class MyAppointmentsServlet extends HttpServlet {

    private final AppointmentService appointmentService = new AppointmentService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        String status = Validator.clean(request.getParameter("status"));

        List<Appointment> appointments = new ArrayList<>();
        for (Appointment a : appointmentService.getAppointmentsFor(user)) {
            if (status.isEmpty() || a.getStatus().equals(status)) {
                appointments.add(a);
            }
        }

        request.setAttribute("appointments", appointments);
        request.setAttribute("status", status);
        // maps so the page can show names instead of ids
        request.setAttribute("serviceNames", serviceManager.getServiceNames());
        request.setAttribute("stylistNames", stylistService.getStylistNames());
        request.setAttribute("userNames", userService.getUserNames());
        request.getRequestDispatcher("/appointment/my-appointments.jsp").forward(request, response);
    }
}
