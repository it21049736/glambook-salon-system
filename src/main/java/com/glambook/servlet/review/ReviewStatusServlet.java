package com.glambook.servlet.review;

import com.glambook.service.ReviewService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin hides or shows a review (POST only).
 */
@WebServlet("/admin/reviews/status")
public class ReviewStatusServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reviewId = request.getParameter("reviewId");
        String status = request.getParameter("status");

        String error = reviewService.setStatus(reviewId, status);
        if (error != null) {
            SessionHelper.setFlashError(request, error);
        } else {
            SessionHelper.setFlash(request, "Review " + reviewId + " was updated.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/reviews");
    }
}
