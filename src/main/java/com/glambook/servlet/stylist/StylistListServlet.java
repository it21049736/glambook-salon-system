package com.glambook.servlet.stylist;

import com.glambook.service.StylistService;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Public stylist list with search by name (?q=) and specialty (?specialty=).
 */
@WebServlet("/stylists")
public class StylistListServlet extends HttpServlet {

    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = Validator.clean(request.getParameter("q"));
        String specialty = Validator.clean(request.getParameter("specialty"));

        request.setAttribute("stylists", stylistService.searchStylists(keyword, specialty));
        request.setAttribute("q", keyword);
        request.setAttribute("specialty", specialty);
        request.getRequestDispatcher("/stylist/stylist-list.jsp").forward(request, response);
    }
}
