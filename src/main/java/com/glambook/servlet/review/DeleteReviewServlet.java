package com.glambook.servlet.review;

import com.glambook.model.User;
import com.glambook.service.ReviewService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Deletes a review (POST only). Customers can delete their own, admins can delete any.
 */
@WebServlet("/reviews/delete")
public class DeleteReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = SessionHelper.getLoggedUser(request);
        String reviewId = request.getParameter("reviewId");

        String error = reviewService.deleteReview(reviewId, user);
        if (error != null) {
            SessionHelper.setFlashError(request, error);
        } else {
            SessionHelper.setFlash(request, "Review " + reviewId + " was deleted.");
        }

        // admins go back to the moderation panel, customers to the reviews page
        String next = user.canAccessAdmin() ? "/admin/reviews" : "/reviews";
        response.sendRedirect(request.getContextPath() + next);
    }
}
