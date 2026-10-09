package com.glambook.servlet.service;

import com.glambook.service.ServiceManager;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Public page that lists services, with search (?q=) and category filter (?category=).
 */
@WebServlet("/services")
public class ServiceListServlet extends HttpServlet {

    private final ServiceManager serviceManager = new ServiceManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = Validator.clean(request.getParameter("q"));
        String category = Validator.clean(request.getParameter("category"));

        request.setAttribute("services", serviceManager.searchServices(keyword, category));
        request.setAttribute("q", keyword);
        request.setAttribute("category", category);
        request.getRequestDispatcher("/service/service-list.jsp").forward(request, response);
    }
}
