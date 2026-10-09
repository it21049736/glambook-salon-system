package com.glambook.servlet.appointment;

import com.glambook.model.Stylist;
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
 * Booking page.
 * GET  /appointments/book?serviceId=..&stylistId=..&date=..  shows the form and the free slots
 * POST /appointments/book                                     saves the booking
 */
@WebServlet("/appointments/book")
public class BookAppointmentServlet extends HttpServlet {

    private final AppointmentService appointmentService = new AppointmentService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        if (user.canAccessAdmin()) {
            SessionHelper.setFlashError(request, "Bookings are made from a customer account.");
            response.sendRedirect(request.getContextPath() + "/appointments/my");
            return;
        }
        prepareForm(request,
                Validator.clean(request.getParameter("serviceId")),
                Validator.clean(request.getParameter("stylistId")),
                Validator.clean(request.getParameter("date")));
        request.getRequestDispatcher("/appointment/book.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        String serviceId = Validator.clean(request.getParameter("serviceId"));
        String stylistId = Validator.clean(request.getParameter("stylistId"));
        String date = Validator.clean(request.getParameter("date"));
        String timeSlot = Validator.clean(request.getParameter("timeSlot"));

        String error = appointmentService.bookAppointment(user.getUserId(), serviceId, stylistId, date, timeSlot);
        if (error != null) {
            request.setAttribute("error", error);
            prepareForm(request, serviceId, stylistId, date);
            request.getRequestDispatcher("/appointment/book.jsp").forward(request, response);
            return;
        }
        SessionHelper.setFlash(request, "Your appointment on " + date + " at " + timeSlot + " is booked!");
        response.sendRedirect(request.getContextPath() + "/appointments/my");
    }

    // puts everything the booking page needs into the request
    private void prepareForm(HttpServletRequest request, String serviceId, String stylistId, String date) {
        request.setAttribute("services", serviceManager.getAllServices());
        request.setAttribute("stylists", stylistService.getAllStylists());
        request.setAttribute("serviceId", serviceId);
        request.setAttribute("stylistId", stylistId);
        request.setAttribute("date", date);
        request.setAttribute("selectedService", serviceManager.getServiceById(serviceId));

        // only check slots once a stylist and a date are chosen
        if (Validator.isEmpty(stylistId) || Validator.isEmpty(date)) {
            return;
        }
        Stylist stylist = stylistService.getStylistById(stylistId);
        if (stylist == null || !Validator.isValidDate(date)) {
            request.setAttribute("slotMessage", "Please choose a valid stylist and date.");
        } else if (!Validator.isNotPastDate(LocalDate.parse(date))) {
            request.setAttribute("slotMessage", "That date is in the past. Please choose today or a later date.");
        } else if (!stylist.worksOn(LocalDate.parse(date).getDayOfWeek())) {
            request.setAttribute("slotMessage", stylist.getName() + " works on " + stylist.getWorkingDays() + " only.");
        } else {
            request.setAttribute("availableSlots", appointmentService.getAvailableSlots(stylistId, LocalDate.parse(date), null));
        }
        request.setAttribute("slotsChecked", true);
    }
}
