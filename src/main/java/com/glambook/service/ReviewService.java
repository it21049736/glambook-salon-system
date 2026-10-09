package com.glambook.service;

import com.glambook.dao.AppointmentDAO;
import com.glambook.dao.ReviewDAO;
import com.glambook.dao.ServiceDAO;
import com.glambook.dao.StylistDAO;
import com.glambook.model.Appointment;
import com.glambook.model.PublicReview;
import com.glambook.model.Review;
import com.glambook.model.User;
import com.glambook.model.VerifiedReview;
import com.glambook.util.IdGenerator;
import com.glambook.util.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Business logic for reviews: submit, view, edit, delete and admin moderation.
 */
public class ReviewService {

    private static final int MAX_COMMENT_LENGTH = 500;

    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final StylistDAO stylistDAO = new StylistDAO();

    // SUBMIT a review; becomes a VerifiedReview if the customer has a completed appointment for it
    public String submitReview(User user, String targetType, String targetId, String rating, String comment) {
        if (user.canAccessAdmin()) {
            return "Reviews are written by customers.";
        }
        if (!targetExists(targetType, targetId)) {
            return "Please choose a service or stylist to review.";
        }
        String error = validate(rating, comment);
        if (error != null) {
            return error;
        }
        // one review per customer for each service/stylist (they can edit it instead)
        for (Review r : reviewDAO.findByTarget(targetType, targetId)) {
            if (r.getCustomerId().equals(user.getUserId())) {
                return "You have already reviewed this. You can edit your review instead.";
            }
        }

        String newId = IdGenerator.generateId(IdGenerator.REVIEW, reviewDAO.getAllIds());
        String appointmentId = findCompletedAppointment(user.getUserId(), targetType, targetId);
        Review review;
        if (appointmentId != null) {
            review = new VerifiedReview(newId, user.getUserId(), user.getFullName(), targetType, targetId,
                    Integer.parseInt(rating), comment, LocalDate.now(), Review.VISIBLE, appointmentId);
        } else {
            review = new PublicReview(newId, user.getUserId(), user.getFullName(), targetType, targetId,
                    Integer.parseInt(rating), comment, LocalDate.now(), Review.VISIBLE);
        }
        return reviewDAO.add(review) ? null : "Could not save the review.";
    }

    // EDIT: only the customer who wrote the review can change the text and rating
    public String editReview(String reviewId, User user, String rating, String comment) {
        Review review = reviewDAO.findById(reviewId);
        if (review == null || !review.getCustomerId().equals(user.getUserId())) {
            return "Review not found.";
        }
        String error = validate(rating, comment);
        if (error != null) {
            return error;
        }
        review.setRating(Integer.parseInt(rating));
        review.setComment(comment);
        review.setReviewDate(LocalDate.now());
        return reviewDAO.update(review) ? null : "Could not update the review.";
    }

    // DELETE: the owner or an admin
    public String deleteReview(String reviewId, User user) {
        Review review = reviewDAO.findById(reviewId);
        if (review == null || !(user.canAccessAdmin() || review.getCustomerId().equals(user.getUserId()))) {
            return "Review not found.";
        }
        return reviewDAO.delete(reviewId) ? null : "Could not delete the review.";
    }

    // MODERATION: admin hides or shows a review
    public String setStatus(String reviewId, String status) {
        Review review = reviewDAO.findById(reviewId);
        if (review == null) {
            return "Review not found.";
        }
        review.setStatus(status);
        return reviewDAO.update(review) ? null : "Could not update the review.";
    }

    public Review getReviewById(String reviewId) {
        return reviewDAO.findById(reviewId);
    }

    // VIEW: visible reviews for one service/stylist, or every visible review if no target is given
    public List<Review> getVisibleReviews(String targetType, String targetId) {
        List<Review> source = Validator.isEmpty(targetId) ? reviewDAO.getAll() : reviewDAO.findByTarget(targetType, targetId);
        List<Review> result = new ArrayList<>();
        for (Review review : source) {
            if (review.isVisible()) {
                result.add(review);
            }
        }
        result.sort(Comparator.comparing(Review::getReviewDate).reversed());
        return result;
    }

    // admin moderation list (hidden ones included)
    public List<Review> getAllReviews() {
        List<Review> all = reviewDAO.getAll();
        all.sort(Comparator.comparing(Review::getReviewDate).reversed());
        return all;
    }

    public double getAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (Review review : reviews) {
            total += review.getRating();
        }
        return (double) total / reviews.size();
    }

    public boolean targetExists(String targetType, String targetId) {
        if (Review.TARGET_SERVICE.equals(targetType)) {
            return serviceDAO.findById(targetId) != null;
        }
        if (Review.TARGET_STYLIST.equals(targetType)) {
            return stylistDAO.findById(targetId) != null;
        }
        return false;
    }

    // looks for a COMPLETED appointment of this customer with the same service or stylist
    private String findCompletedAppointment(String customerId, String targetType, String targetId) {
        for (Appointment a : appointmentDAO.findByCustomer(customerId)) {
            boolean sameTarget = Review.TARGET_SERVICE.equals(targetType)
                    ? a.getServiceId().equals(targetId)
                    : a.getStylistId().equals(targetId);
            if (sameTarget && Appointment.COMPLETED.equals(a.getStatus())) {
                return a.getAppointmentId();
            }
        }
        return null;
    }

    private String validate(String rating, String comment) {
        if (!Validator.isPositiveInteger(rating) || Integer.parseInt(rating) > 5) {
            return "Please give a rating from 1 to 5 stars.";
        }
        if (Validator.isEmpty(comment)) {
            return "Please write a short comment.";
        }
        if (comment.length() > MAX_COMMENT_LENGTH) {
            return "Comment is too long (maximum " + MAX_COMMENT_LENGTH + " characters).";
        }
        return null;
    }
}
