package com.glambook.servlet.appointment;

import com.glambook.model.Appointment;
import com.glambook.model.User;
import com.glambook.service.AppointmentService;
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
import java.time.LocalDate;

/**
 * Reschedule an appointment.
 * GET  ?id=A001&date=2026-10-20  shows the current booking and the free slots on the new date
 * POST                            saves the new date and time
 */
@WebServlet("/appointments/reschedule")
public class RescheduleServlet extends HttpServlet {

    private final AppointmentService appointmentService = new AppointmentService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = Validator.clean(request.getParameter("id"));
        if (!loadAppointment(request, response, id)) {
            return;
        }
        prepareSlots(request, id, Validator.clean(request.getParameter("date")));
        request.getRequestDispatcher("/appointment/reschedule.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        String id = Validator.clean(request.getParameter("appointmentId"));
        String date = Validator.clean(request.getParameter("date"));
        String timeSlot = Validator.clean(request.getParameter("timeSlot"));

        String error = appointmentService.reschedule(id, user, date, timeSlot);
        if (error != null) {
            if (!loadAppointment(request, response, id)) {
                return;
            }
            request.setAttribute("error", error);
            prepareSlots(request, id, date);
            request.getRequestDispatcher("/appointment/reschedule.jsp").forward(request, response);
            return;
        }
        SessionHelper.setFlash(request, "Appointment " + id + " moved to " + date + " at " + timeSlot + ".");
        response.sendRedirect(request.getContextPath() + "/appointments/my");
    }

    // loads the appointment and checks the user may change it; redirects if not
    private boolean loadAppointment(HttpServletRequest request, HttpServletResponse response, String id)
            throws IOException {
        Appointment appointment = appointmentService.getAppointmentById(id);
        User user = SessionHelper.getLoggedUser(request);
        if (appointment == null || !appointmentService.canManage(user, appointment) || !appointment.isChangeable()) {
            SessionHelper.setFlashError(request, "This appointment cannot be rescheduled.");
            response.sendRedirect(request.getContextPath() + "/appointments/my");
            return false;
        }
        request.setAttribute("appointment", appointment);
        request.setAttribute("service", serviceManager.getServiceById(appointment.getServiceId()));
        request.setAttribute("stylist", stylistService.getStylistById(appointment.getStylistId()));
        return true;
    }

    private void prepareSlots(HttpServletRequest request, String id, String date) {
        request.setAttribute("date", date);
        if (Validator.isValidDate(date)) {
            Appointment appointment = (Appointment) request.getAttribute("appointment");
            // the appointment's own id is passed so its current slot counts as free
            request.setAttribute("availableSlots",
                    appointmentService.getAvailableSlots(appointment.getStylistId(), LocalDate.parse(date), id));
            request.setAttribute("slotsChecked", true);
        }
    }
}
