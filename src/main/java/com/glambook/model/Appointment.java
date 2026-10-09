package com.glambook.model;

import java.time.LocalDate;

/**
 * A booking made by a customer for one service with one stylist at a date and time slot.
 * One line in appointments.txt:
 * appointmentId|customerId|serviceId|stylistId|date|timeSlot|originalPrice|discount|finalPrice|status
 */
public class Appointment {

    public static final String BOOKED = "BOOKED";
    public static final String RESCHEDULED = "RESCHEDULED";
    public static final String CANCELLED = "CANCELLED";
    public static final String COMPLETED = "COMPLETED";

    // ENCAPSULATION: all fields are private; setters check the values before saving them
    private String appointmentId;
    private String customerId;
    private String serviceId;
    private String stylistId;
    private LocalDate date;
    private String timeSlot;
    private double originalPrice;
    private double discount;
    private double finalPrice;
    private String status;

    public Appointment(String appointmentId, String customerId, String serviceId, String stylistId,
                       LocalDate date, String timeSlot, double originalPrice, double discount,
                       double finalPrice, String status) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.stylistId = stylistId;
        this.date = date;
        this.timeSlot = timeSlot;
        setOriginalPrice(originalPrice);
        setDiscount(discount);
        setFinalPrice(finalPrice);
        setStatus(status);
    }

    // BOOKED and RESCHEDULED appointments still hold a time slot
    public boolean isActive() {
        return BOOKED.equals(status) || RESCHEDULED.equals(status);
    }

    // an active appointment that has not passed can still be rescheduled or cancelled
    public boolean isChangeable() {
        return isActive() && !date.isBefore(LocalDate.now());
    }

    public String toFileString() {
        return appointmentId + "|" + customerId + "|" + serviceId + "|" + stylistId + "|" + date + "|"
                + timeSlot + "|" + originalPrice + "|" + discount + "|" + finalPrice + "|" + status;
    }

    // returns null if the line is broken so the DAO can skip it
    public static Appointment fromFileString(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 10) {
                return null;
            }
            return new Appointment(p[0], p[1], p[2], p[3], LocalDate.parse(p[4]), p[5],
                    Double.parseDouble(p[6]), Double.parseDouble(p[7]), Double.parseDouble(p[8]), p[9].trim());
        } catch (Exception e) {
            return null;
        }
    }

    // ---------- getters and setters ----------

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getStylistId() {
        return stylistId;
    }

    public void setStylistId(String stylistId) {
        this.stylistId = stylistId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    // prices can never be negative
    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = Math.max(0, originalPrice);
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = Math.max(0, discount);
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = Math.max(0, finalPrice);
    }

    public String getStatus() {
        return status;
    }

    // only the four known statuses are accepted
    public void setStatus(String status) {
        if (RESCHEDULED.equals(status) || CANCELLED.equals(status) || COMPLETED.equals(status)) {
            this.status = status;
        } else {
            this.status = BOOKED;
        }
    }
}
