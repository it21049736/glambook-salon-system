package com.glambook.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * A stylist who works at the salon.
 * One line in stylists.txt:
 * stylistId|level|name|phone|email|specialty|workingDays|shiftStart|shiftEnd|experienceYears
 * workingDays is a comma list such as MON,TUE,WED and shift times are HH:mm.
 */
// ABSTRACTION: Stylist is abstract, every stylist is either Senior or Junior
public abstract class Stylist {

    // ENCAPSULATION: private fields with getters and setters
    private String stylistId;
    private String name;
    private String phone;
    private String email;
    private String specialty;      // HAIR, SKIN or MAKEUP (same as the service categories)
    private String workingDays;
    private String shiftStart;
    private String shiftEnd;
    private int experienceYears;

    public Stylist(String stylistId, String name, String phone, String email, String specialty,
                   String workingDays, String shiftStart, String shiftEnd, int experienceYears) {
        this.stylistId = stylistId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialty = specialty;
        this.workingDays = workingDays;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.experienceYears = experienceYears;
    }

    // ABSTRACTION: SENIOR or JUNIOR
    public abstract String getLevel();

    // POLYMORPHISM: each level earns a different commission rate
    public abstract double getCommissionRate();

    // POLYMORPHISM: commission calculation is overridden by the subclasses
    public abstract double calculateCommission(double serviceAmount);

    // true if the stylist works on the given day, e.g. MONDAY -> "MON"
    public boolean worksOn(DayOfWeek day) {
        String shortDay = day.name().substring(0, 3);
        return workingDays != null && workingDays.contains(shortDay);
    }

    // true if a time slot (e.g. 10:00) starts inside the stylist's shift
    public boolean isWithinShift(String time) {
        try {
            LocalTime slot = LocalTime.parse(time);
            return !slot.isBefore(LocalTime.parse(shiftStart)) && slot.isBefore(LocalTime.parse(shiftEnd));
        } catch (Exception e) {
            return false;
        }
    }

    public String toFileString() {
        return stylistId + "|" + getLevel() + "|" + name + "|" + phone + "|" + email + "|" + specialty + "|"
                + workingDays + "|" + shiftStart + "|" + shiftEnd + "|" + experienceYears;
    }

    // reads one line and creates SeniorStylist or JuniorStylist; null for a broken line
    public static Stylist fromFileString(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 10) {
                return null;
            }
            return create(p[1].trim(), p[0], p[2], p[3], p[4], p[5], p[6], p[7], p[8], Integer.parseInt(p[9]));
        } catch (Exception e) {
            return null;
        }
    }

    // factory method that picks the subclass from the level
    public static Stylist create(String level, String id, String name, String phone, String email, String specialty,
                                 String workingDays, String shiftStart, String shiftEnd, int experienceYears) {
        if (SeniorStylist.LEVEL.equals(level)) {
            return new SeniorStylist(id, name, phone, email, specialty, workingDays, shiftStart, shiftEnd, experienceYears);
        } else if (JuniorStylist.LEVEL.equals(level)) {
            return new JuniorStylist(id, name, phone, email, specialty, workingDays, shiftStart, shiftEnd, experienceYears);
        }
        return null;
    }

    // ---------- getters and setters ----------

    public String getStylistId() {
        return stylistId;
    }

    public void setStylistId(String stylistId) {
        this.stylistId = stylistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(String shiftStart) {
        this.shiftStart = shiftStart;
    }

    public String getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(String shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
}
