package com.glambook.model;

import java.time.LocalDate;

/**
 * A review from a customer who really completed an appointment for this service or stylist.
 */
// INHERITANCE: VerifiedReview is a Review with an extra appointmentId
public class VerifiedReview extends Review {

    public static final String TYPE = "VERIFIED";

    private String appointmentId;

    public VerifiedReview(String reviewId, String customerId, String customerName, String targetType,
                          String targetId, int rating, String comment, LocalDate reviewDate, String status,
                          String appointmentId) {
        super(reviewId, customerId, customerName, targetType, targetId, rating, comment, reviewDate, status);
        this.appointmentId = appointmentId;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getAppointmentId() {
        return appointmentId;
    }

    // METHOD OVERRIDING: verified reviews show a trusted badge
    @Override
    public String getBadge() {
        return "Verified Visit";
    }

    // METHOD OVERRIDING: admins also see which appointment proves the visit
    @Override
    public String getDisplayName(boolean forAdmin) {
        String name = super.getDisplayName(forAdmin);
        return forAdmin ? name + " - booking " + appointmentId : name;
    }
}
