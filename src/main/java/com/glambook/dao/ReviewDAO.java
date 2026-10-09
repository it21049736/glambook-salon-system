package com.glambook.dao;

import com.glambook.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes reviews in reviews.txt.
 */
public class ReviewDAO {

    private static final String FILE_NAME = "reviews.txt";

    public List<Review> getAll() {
        List<Review> reviews = new ArrayList<>();
        for (String line : FileHandler.readLines(FILE_NAME)) {
            Review review = Review.fromFileString(line);
            if (review != null) {
                reviews.add(review);
            }
        }
        return reviews;
    }

    public Review findById(String reviewId) {
        for (Review review : getAll()) {
            if (review.getReviewId().equals(reviewId)) {
                return review;
            }
        }
        return null;
    }

    // all reviews for one service or one stylist
    public List<Review> findByTarget(String targetType, String targetId) {
        List<Review> result = new ArrayList<>();
        for (Review review : getAll()) {
            if (review.getTargetType().equals(targetType) && review.getTargetId().equals(targetId)) {
                result.add(review);
            }
        }
        return result;
    }

    public List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        for (Review review : getAll()) {
            ids.add(review.getReviewId());
        }
        return ids;
    }

    // CREATE
    public boolean add(Review review) {
        return FileHandler.appendLine(FILE_NAME, review.toFileString());
    }

    // UPDATE (edit text/rating or change visibility)
    public boolean update(Review updated) {
        List<Review> reviews = getAll();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getReviewId().equals(updated.getReviewId())) {
                reviews.set(i, updated);
                return saveAll(reviews);
            }
        }
        return false;
    }

    // DELETE
    public boolean delete(String reviewId) {
        List<Review> reviews = getAll();
        boolean removed = reviews.removeIf(r -> r.getReviewId().equals(reviewId));
        return removed && saveAll(reviews);
    }

    private boolean saveAll(List<Review> reviews) {
        List<String> lines = new ArrayList<>();
        for (Review review : reviews) {
            lines.add(review.toFileString());
        }
        return FileHandler.writeLines(FILE_NAME, lines);
    }
}
