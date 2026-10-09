package com.glambook.service;

import com.glambook.dao.StylistDAO;
import com.glambook.model.Stylist;
import com.glambook.util.IdGenerator;
import com.glambook.util.Validator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic for stylists: add, search, update profile/schedule and remove.
 */
public class StylistService {

    private final StylistDAO stylistDAO = new StylistDAO();

    // ADD a new stylist; returns an error message or null on success
    public String addStylist(String level, String name, String phone, String email, String specialty,
                             String workingDays, String shiftStart, String shiftEnd, String experience) {
        String error = validate(name, phone, email, specialty, workingDays, shiftStart, shiftEnd, experience);
        if (error != null) {
            return error;
        }
        String newId = IdGenerator.generateId(IdGenerator.STYLIST, stylistDAO.getAllIds());
        Stylist stylist = Stylist.create(level, newId, name, phone, email.toLowerCase(), specialty,
                workingDays, shiftStart, shiftEnd, Integer.parseInt(experience));
        if (stylist == null) {
            return "Please choose Senior or Junior level.";
        }
        return stylistDAO.add(stylist) ? null : "Could not save the stylist.";
    }

    // UPDATE profile and schedule. A new object is created so the level (subclass) can change too.
    public String updateStylist(String stylistId, String level, String name, String phone, String email,
                                String specialty, String workingDays, String shiftStart, String shiftEnd,
                                String experience) {
        if (stylistDAO.findById(stylistId) == null) {
            return "Stylist not found.";
        }
        String error = validate(name, phone, email, specialty, workingDays, shiftStart, shiftEnd, experience);
        if (error != null) {
            return error;
        }
        Stylist updated = Stylist.create(level, stylistId, name, phone, email.toLowerCase(), specialty,
                workingDays, shiftStart, shiftEnd, Integer.parseInt(experience));
        if (updated == null) {
            return "Please choose Senior or Junior level.";
        }
        return stylistDAO.update(updated) ? null : "Could not update the stylist.";
    }

    public boolean deleteStylist(String stylistId) {
        return stylistDAO.delete(stylistId);
    }

    public Stylist getStylistById(String stylistId) {
        return stylistDAO.findById(stylistId);
    }

    public List<Stylist> getAllStylists() {
        return stylistDAO.getAll();
    }

    // SEARCH by name (keyword) and/or specialty
    public List<Stylist> searchStylists(String keyword, String specialty) {
        List<Stylist> result = new ArrayList<>();
        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        for (Stylist stylist : stylistDAO.getAll()) {
            boolean matchesName = key.isEmpty() || stylist.getName().toLowerCase().contains(key);
            boolean matchesSpecialty = Validator.isEmpty(specialty) || stylist.getSpecialty().equals(specialty);
            if (matchesName && matchesSpecialty) {
                result.add(stylist);
            }
        }
        return result;
    }

    // id -> name map for showing names on other pages
    public Map<String, String> getStylistNames() {
        Map<String, String> names = new HashMap<>();
        for (Stylist stylist : stylistDAO.getAll()) {
            names.put(stylist.getStylistId(), stylist.getName());
        }
        return names;
    }

    private String validate(String name, String phone, String email, String specialty, String workingDays,
                            String shiftStart, String shiftEnd, String experience) {
        if (Validator.isEmpty(name) || Validator.isEmpty(specialty)) {
            return "Name and specialty are required.";
        }
        if (!Validator.isValidPhone(phone)) {
            return "Phone number must have 10 digits and start with 0.";
        }
        if (!Validator.isValidEmail(email)) {
            return "Please enter a valid email address.";
        }
        if (Validator.isEmpty(workingDays)) {
            return "Select at least one working day.";
        }
        try {
            if (!LocalTime.parse(shiftStart).isBefore(LocalTime.parse(shiftEnd))) {
                return "Shift end time must be after the start time.";
            }
        } catch (Exception e) {
            return "Please choose valid shift times.";
        }
        try {
            int years = Integer.parseInt(experience);
            if (years < 0 || years > 50) {
                return "Experience must be between 0 and 50 years.";
            }
        } catch (NumberFormatException e) {
            return "Experience must be a whole number.";
        }
        return null;
    }
}
