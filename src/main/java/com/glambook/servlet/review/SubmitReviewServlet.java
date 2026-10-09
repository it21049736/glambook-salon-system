package com.glambook.servlet.review;

import com.glambook.model.User;
import com.glambook.service.ReviewService;
import com.glambook.service.ServiceManager;
import com.glambook.service.StylistService;
import com.glambook.servlet.common.SessionHelper;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Customer writes a review. GET shows the form (?type=&id= pre-selects the target), POST saves it.
 */
@WebServlet("/reviews/submit")
public class SubmitReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("target", Validator.clean(request.getParameter("type")) + ":"
                + Validator.clean(request.getParameter("id")));
        showForm(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = SessionHelper.getLoggedUser(request);

        // the select box sends "SERVICE:S001" or "STYLIST:ST001", so split it into type and id
        String target = Validator.clean(request.getParameter("target"));
        String[] parts = target.split(":");
        String type = parts.length == 2 ? parts[0] : "";
        String id = parts.length == 2 ? parts[1] : "";

        String error = reviewService.submitReview(user, type, id,
                Validator.clean(request.getParameter("rating")),
                Validator.clean(request.getParameter("comment")));

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("target", target);
            showForm(request, response);
            return;
        }
        SessionHelper.setFlash(request, "Thank you! Your review has been posted.");
        response.sendRedirect(request.getContextPath() + "/reviews?type=" + type + "&id=" + id);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("services", serviceManager.getAllServices());
        request.setAttribute("stylists", stylistService.getAllStylists());
        request.getRequestDispatcher("/review/submit-review.jsp").forward(request, response);
    }
}
