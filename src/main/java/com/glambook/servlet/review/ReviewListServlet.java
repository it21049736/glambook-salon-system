package com.glambook.servlet.review;

import com.glambook.model.Review;
import com.glambook.service.ReviewService;
import com.glambook.service.ServiceManager;
import com.glambook.service.StylistService;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Public review page.
 * /reviews                          latest reviews
 * /reviews?type=SERVICE&id=S001     reviews of one service
 * /reviews?type=STYLIST&id=ST001    reviews of one stylist
 */
@WebServlet("/reviews")
public class ReviewListServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = Validator.clean(request.getParameter("type"));
        String id = Validator.clean(request.getParameter("id"));

        List<Review> reviews = reviewService.getVisibleReviews(type, id);
        request.setAttribute("reviews", reviews);
        request.setAttribute("averageRating", reviewService.getAverageRating(reviews));
        request.setAttribute("type", type);
        request.setAttribute("id", id);

        // lists for the "choose a service / stylist" selector and to show names instead of ids
        request.setAttribute("services", serviceManager.getAllServices());
        request.setAttribute("stylists", stylistService.getAllStylists());
        request.setAttribute("serviceNames", serviceManager.getServiceNames());
        request.setAttribute("stylistNames", stylistService.getStylistNames());
        request.getRequestDispatcher("/review/reviews.jsp").forward(request, response);
    }
}
