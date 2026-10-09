package com.glambook.servlet.service;

import com.glambook.service.ServiceManager;
import com.glambook.servlet.common.SessionHelper;
import com.glambook.util.Validator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin adds a new service. GET shows the form, POST saves it.
 */
@WebServlet("/admin/services/add")
public class AddServiceServlet extends HttpServlet {

    private final ServiceManager serviceManager = new ServiceManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/service/add-service.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String category = Validator.clean(request.getParameter("category"));
        String name = Validator.clean(request.getParameter("name"));
        String price = Validator.clean(request.getParameter("basePrice"));
        String duration = Validator.clean(request.getParameter("duration"));
        String description = Validator.clean(request.getParameter("description"));
        // the extra field depends on the category chosen in the form
        String extra = Validator.clean(request.getParameter("extra_" + category));

        String error = serviceManager.addService(category, name, price, duration, description, extra);
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("category", category);
            request.setAttribute("name", name);
            request.setAttribute("basePrice", price);
            request.setAttribute("duration", duration);
            request.setAttribute("description", description);
            request.getRequestDispatcher("/service/add-service.jsp").forward(request, response);
            return;
        }
        SessionHelper.setFlash(request, "Service \"" + name + "\" was added.");
        response.sendRedirect(request.getContextPath() + "/services");
    }
}
