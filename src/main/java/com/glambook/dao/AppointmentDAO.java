package com.glambook.dao;

import com.glambook.model.Appointment;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes appointments in appointments.txt.
 */
public class AppointmentDAO {

    private static final String FILE_NAME = "appointments.txt";

    public List<Appointment> getAll() {
        List<Appointment> appointments = new ArrayList<>();
        for (String line : FileHandler.readLines(FILE_NAME)) {
            Appointment appointment = Appointment.fromFileString(line);
            if (appointment != null) {
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public Appointment findById(String appointmentId) {
        for (Appointment appointment : getAll()) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }

    public List<Appointment> findByCustomer(String customerId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : getAll()) {
            if (appointment.getCustomerId().equals(customerId)) {
                result.add(appointment);
            }
        }
        return result;
    }

    public List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        for (Appointment appointment : getAll()) {
            ids.add(appointment.getAppointmentId());
        }
        return ids;
    }

    // CREATE
    public boolean add(Appointment appointment) {
        return FileHandler.appendLine(FILE_NAME, appointment.toFileString());
    }

    // UPDATE (used for reschedule, cancel and complete)
    public boolean update(Appointment updated) {
        List<Appointment> appointments = getAll();
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(updated.getAppointmentId())) {
                appointments.set(i, updated);
                return saveAll(appointments);
            }
        }
        return false;
    }

    // DELETE
    public boolean delete(String appointmentId) {
        List<Appointment> appointments = getAll();
        boolean removed = appointments.removeIf(a -> a.getAppointmentId().equals(appointmentId));
        return removed && saveAll(appointments);
    }

    private boolean saveAll(List<Appointment> appointments) {
        List<String> lines = new ArrayList<>();
        for (Appointment appointment : appointments) {
            lines.add(appointment.toFileString());
        }
        return FileHandler.writeLines(FILE_NAME, lines);
    }
}
