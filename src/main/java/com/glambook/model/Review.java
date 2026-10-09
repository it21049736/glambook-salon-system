package com.glambook.model;

import java.time.LocalDate;

/**
 * A customer review about a service or a stylist.
 * One line in reviews.txt:
 * reviewId|type|customerId|customerName|targetType|targetId|rating|comment|reviewDate|status|appointmentId
 */
// ABSTRACTION: Review is the base class; a real review is a PublicReview or a VerifiedReview
public abstract class Review {

    public static final String TARGET_SERVICE = "SERVICE";
    public static final String TARGET_STYLIST = "STYLIST";
    public static final String VISIBLE = "VISIBLE";
    public static final String HIDDEN = "HIDDEN";

    // ENCAPSULATION: private fields with getters and setters
    private String reviewId;
    private String customerId;
    private String customerName;
    private String targetType;
    private String targetId;
    private int rating;
    private String comment;
    private LocalDate reviewDate;
    private String status;

    public Review(String reviewId, String customerId, String customerName, String targetType, String targetId,
                  int rating, String comment, LocalDate reviewDate, String status) {
        this.reviewId = reviewId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.targetType = targetType;
        this.targetId = targetId;
        setRating(rating);
        this.comment = comment;
        this.reviewDate = reviewDate;
        setStatus(status);
    }

    // PUBLIC or VERIFIED
    public abstract String getType();

    // the appointment that proves the visit ("-" when there is none)
    public abstract String getAppointmentId();

    // METHOD OVERRIDING: subclasses show a different badge
    public String getBadge() {
        return "Review";
    }

    // POLYMORPHISM: different display for admin and for normal users
    // admin sees the full name and customer id, other users only see "Nimali P."
    public String getDisplayName(boolean forAdmin) {
        if (forAdmin) {
            return customerName + " (" + customerId + ")";
        }
        String[] parts = customerName.trim().split(" ");
        if (parts.length > 1) {
            return parts[0] + " " + parts[parts.length - 1].charAt(0) + ".";
        }
        return parts[0];
    }

    public boolean isVisible() {
        return VISIBLE.equals(status);
    }

    public String toFileString() {
        return reviewId + "|" + getType() + "|" + customerId + "|" + customerName + "|" + targetType + "|"
                + targetId + "|" + rating + "|" + comment + "|" + reviewDate + "|" + status + "|" + getAppointmentId();
    }

    // reads one line and creates the right subclass; null for a broken line
    public static Review fromFileString(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 11) {
                return null;
            }
            int rating = Integer.parseInt(p[6]);
            LocalDate date = LocalDate.parse(p[8]);
            if (VerifiedReview.TYPE.equals(p[1])) {
                return new VerifiedReview(p[0], p[2], p[3], p[4], p[5], rating, p[7], date, p[9], p[10]);
            } else if (PublicReview.TYPE.equals(p[1])) {
                return new PublicReview(p[0], p[2], p[3], p[4], p[5], rating, p[7], date, p[9]);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // ---------- getters and setters ----------

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getRating() {
        return rating;
    }

    // rating is always kept between 1 and 5
    public void setRating(int rating) {
        this.rating = Math.max(1, Math.min(5, rating));
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = HIDDEN.equals(status) ? HIDDEN : VISIBLE;
    }
}
