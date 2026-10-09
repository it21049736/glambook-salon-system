package com.glambook.servlet.review;

import com.glambook.service.ReviewService;
import com.glambook.service.ServiceManager;
import com.glambook.service.StylistService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin moderation panel: every review, including hidden ones.
 */
@WebServlet("/admin/reviews")
public class ReviewModerationServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("reviews", reviewService.getAllReviews());
        request.setAttribute("serviceNames", serviceManager.getServiceNames());
        request.setAttribute("stylistNames", stylistService.getStylistNames());
        request.getRequestDispatcher("/review/moderation.jsp").forward(request, response);
    }
}
