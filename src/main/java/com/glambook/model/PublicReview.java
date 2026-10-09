package com.glambook.model;

import java.time.LocalDate;

/**
 * A review from a customer who has no completed appointment for the service or stylist.
 */
// INHERITANCE: PublicReview is a Review
public class PublicReview extends Review {

    public static final String TYPE = "PUBLIC";

    public PublicReview(String reviewId, String customerId, String customerName, String targetType,
                        String targetId, int rating, String comment, LocalDate reviewDate, String status) {
        super(reviewId, customerId, customerName, targetType, targetId, rating, comment, reviewDate, status);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getAppointmentId() {
        return "-";
    }

    // METHOD OVERRIDING: public reviews get a plain badge
    @Override
    public String getBadge() {
        return "Customer Review";
    }
}
