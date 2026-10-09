package com.glambook.servlet.review;

import com.glambook.model.Review;
import com.glambook.model.User;
import com.glambook.service.ReviewService;
import com.glambook.servlet.common.SessionHelper;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Customer edits their own review. GET ?id=R001 shows the form, POST saves it.
 */
@WebServlet("/reviews/edit")
public class EditReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        Review review = reviewService.getReviewById(request.getParameter("id"));
        if (review == null || !review.getCustomerId().equals(user.getUserId())) {
            SessionHelper.setFlashError(request, "You can only edit your own reviews.");
            response.sendRedirect(request.getContextPath() + "/reviews");
            return;
        }
        request.setAttribute("review", review);
        request.getRequestDispatcher("/review/edit-review.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);
        String reviewId = Validator.clean(request.getParameter("reviewId"));

        String error = reviewService.editReview(reviewId, user,
                Validator.clean(request.getParameter("rating")),
                Validator.clean(request.getParameter("comment")));

        Review review = reviewService.getReviewById(reviewId);
        if (error != null) {
            if (review == null) {
                SessionHelper.setFlashError(request, error);
                response.sendRedirect(request.getContextPath() + "/reviews");
                return;
            }
            request.setAttribute("error", error);
            request.setAttribute("review", review);
            request.getRequestDispatcher("/review/edit-review.jsp").forward(request, response);
            return;
        }
        SessionHelper.setFlash(request, "Your review was updated.");
        response.sendRedirect(request.getContextPath() + "/reviews?type=" + review.getTargetType()
                + "&id=" + review.getTargetId());
    }
}
