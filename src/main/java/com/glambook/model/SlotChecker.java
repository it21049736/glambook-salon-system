package com.glambook.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Anything that can tell us whether a stylist is free at a certain time.
 * AppointmentService implements this interface.
 */
// ABSTRACTION: an interface only says WHAT must be done, the class decides HOW
public interface SlotChecker {

    // the salon works in one hour slots
    String[] TIME_SLOTS = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
            "14:00", "15:00", "16:00", "17:00", "18:00", "19:00"};

    // ignoreAppointmentId lets an appointment keep its own slot while it is being rescheduled
    boolean isSlotAvailable(String stylistId, LocalDate date, String timeSlot, String ignoreAppointmentId);

    List<String> getAvailableSlots(String stylistId, LocalDate date, String ignoreAppointmentId);
}
