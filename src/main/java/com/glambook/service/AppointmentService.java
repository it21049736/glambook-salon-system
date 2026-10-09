package com.glambook.service;

import com.glambook.dao.AppointmentDAO;
import com.glambook.dao.ServiceDAO;
import com.glambook.dao.StylistDAO;
import com.glambook.dao.UserDAO;
import com.glambook.model.Appointment;
import com.glambook.model.Customer;
import com.glambook.model.DiscountPolicy;
import com.glambook.model.PremiumDiscount;
import com.glambook.model.RegularDiscount;
import com.glambook.model.Service;
import com.glambook.model.SlotChecker;
import com.glambook.model.Stylist;
import com.glambook.model.User;
import com.glambook.util.IdGenerator;
import com.glambook.util.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Business logic for booking, rescheduling and cancelling appointments.
 * Implements SlotChecker, so it is also responsible for checking free time slots.
 */
public class AppointmentService implements SlotChecker {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final StylistDAO stylistDAO = new StylistDAO();
    private final UserDAO userDAO = new UserDAO();

    // ---------- SlotChecker methods ----------

    // a slot is free if no other active appointment has the same stylist, date and time
    @Override
    public boolean isSlotAvailable(String stylistId, LocalDate date, String timeSlot, String ignoreAppointmentId) {
        for (Appointment a : appointmentDAO.getAll()) {
            boolean sameSlot = a.getStylistId().equals(stylistId)
                    && a.getDate().equals(date)
                    && a.getTimeSlot().equals(timeSlot);
            boolean isItself = a.getAppointmentId().equals(ignoreAppointmentId);
            if (sameSlot && a.isActive() && !isItself) {
                return false;
            }
        }
        return true;
    }

    // all free slots for a stylist on a date: inside the shift, not booked and not already over
    @Override
    public List<String> getAvailableSlots(String stylistId, LocalDate date, String ignoreAppointmentId) {
        List<String> freeSlots = new ArrayList<>();
        Stylist stylist = stylistDAO.findById(stylistId);
        if (stylist == null || date == null || date.isBefore(LocalDate.now()) || !stylist.worksOn(date.getDayOfWeek())) {
            return freeSlots;
        }
        for (String slot : TIME_SLOTS) {
            boolean alreadyPassed = date.equals(LocalDate.now()) && !LocalTime.parse(slot).isAfter(LocalTime.now());
            if (stylist.isWithinShift(slot) && !alreadyPassed
                    && isSlotAvailable(stylistId, date, slot, ignoreAppointmentId)) {
                freeSlots.add(slot);
            }
        }
        return freeSlots;
    }

    // ---------- CRUD ----------

    // BOOK a new appointment; returns an error message or null on success
    public String bookAppointment(String customerId, String serviceId, String stylistId, String dateText, String timeSlot) {
        if (Validator.isEmpty(serviceId) || Validator.isEmpty(stylistId)
                || Validator.isEmpty(dateText) || Validator.isEmpty(timeSlot)) {
            return "Please choose a service, stylist, date and time slot.";
        }
        User user = userDAO.findById(customerId);
        if (!(user instanceof Customer)) {
            return "Only customers can book appointments.";
        }
        Service service = serviceDAO.findById(serviceId);
        Stylist stylist = stylistDAO.findById(stylistId);
        if (service == null || stylist == null) {
            return "The selected service or stylist no longer exists.";
        }
        if (!stylist.getSpecialty().equals(service.getCategory())) {
            return stylist.getName() + " does not do " + service.getCategory().toLowerCase() + " services.";
        }
        String error = checkDateAndSlot(stylist, dateText, timeSlot, null);
        if (error != null) {
            return error;
        }

        // POLYMORPHISM: calculatePrice() and calculateDiscount() run the subclass version
        double price = service.calculatePrice();
        DiscountPolicy policy = getDiscountPolicy((Customer) user);
        double discount = policy.calculateDiscount(price);

        String newId = IdGenerator.generateId(IdGenerator.APPOINTMENT, appointmentDAO.getAllIds());
        Appointment appointment = new Appointment(newId, customerId, serviceId, stylistId, LocalDate.parse(dateText),
                timeSlot, price, discount, price - discount, Appointment.BOOKED);
        return appointmentDAO.add(appointment) ? null : "Could not save the appointment.";
    }

    // RESCHEDULE to a new date and time with the same stylist
    public String reschedule(String appointmentId, User user, String dateText, String timeSlot) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null || !canManage(user, appointment)) {
            return "Appointment not found.";
        }
        if (!appointment.isChangeable()) {
            return "Only upcoming appointments can be rescheduled.";
        }
        Stylist stylist = stylistDAO.findById(appointment.getStylistId());
        if (stylist == null) {
            return "The stylist for this appointment is no longer available.";
        }
        // the appointment's own slot is ignored, so moving it by a few hours works
        String error = checkDateAndSlot(stylist, dateText, timeSlot, appointmentId);
        if (error != null) {
            return error;
        }
        appointment.setDate(LocalDate.parse(dateText));
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus(Appointment.RESCHEDULED);
        return appointmentDAO.update(appointment) ? null : "Could not update the appointment.";
    }

    // CANCEL keeps the record (for history) but frees the time slot
    public String cancel(String appointmentId, User user) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null || !canManage(user, appointment)) {
            return "Appointment not found.";
        }
        if (!appointment.isChangeable()) {
            return "This appointment can no longer be cancelled.";
        }
        appointment.setStatus(Appointment.CANCELLED);
        return appointmentDAO.update(appointment) ? null : "Could not cancel the appointment.";
    }

    // used by the payment module after a successful payment
    public boolean markCompleted(String appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            return false;
        }
        appointment.setStatus(Appointment.COMPLETED);
        return appointmentDAO.update(appointment);
    }

    public Appointment getAppointmentById(String appointmentId) {
        return appointmentDAO.findById(appointmentId);
    }

    // customers see their own appointments, admins see everyone's (newest date first)
    public List<Appointment> getAppointmentsFor(User user) {
        List<Appointment> list = user.canAccessAdmin()
                ? appointmentDAO.getAll()
                : appointmentDAO.findByCustomer(user.getUserId());
        list.sort(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTimeSlot).reversed());
        return list;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAll();
    }

    // only the owner of the appointment or an admin may change it
    public boolean canManage(User user, Appointment appointment) {
        return user != null && (user.canAccessAdmin() || appointment.getCustomerId().equals(user.getUserId()));
    }

    // POLYMORPHISM: the same DiscountPolicy variable can hold either kind of discount
    private DiscountPolicy getDiscountPolicy(Customer customer) {
        if (customer.isPremium()) {
            return new PremiumDiscount();
        }
        return new RegularDiscount();
    }

    // shared date and slot checks for booking and rescheduling
    private String checkDateAndSlot(Stylist stylist, String dateText, String timeSlot, String ignoreAppointmentId) {
        if (!Validator.isValidDate(dateText)) {
            return "Please choose a valid date.";
        }
        LocalDate date = LocalDate.parse(dateText);
        if (!Validator.isNotPastDate(date)) {
            return "You cannot book an appointment in the past.";
        }
        if (!stylist.worksOn(date.getDayOfWeek())) {
            return stylist.getName() + " does not work on " + date.getDayOfWeek().toString().toLowerCase() + "s.";
        }
        if (!getAvailableSlots(stylist.getStylistId(), date, ignoreAppointmentId).contains(timeSlot)) {
            return "Sorry, " + timeSlot + " on " + date + " is not available for " + stylist.getName()
                    + ". Please pick another slot.";
        }
        return null;
    }
}
