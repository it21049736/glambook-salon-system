package com.glambook.servlet.service;

import com.glambook.model.Service;
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
 * Admin edits a service (price, duration, etc). GET ?id=S001 shows the form, POST saves it.
 */
@WebServlet("/admin/services/edit")
public class EditServiceServlet extends HttpServlet {

    private final ServiceManager serviceManager = new ServiceManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Service service = serviceManager.getServiceById(request.getParameter("id"));
        if (service == null) {
            SessionHelper.setFlashError(request, "Service not found.");
            response.sendRedirect(request.getContextPath() + "/services");
            return;
        }
        request.setAttribute("service", service);
        request.getRequestDispatcher("/service/edit-service.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String serviceId = Validator.clean(request.getParameter("serviceId"));
        String name = Validator.clean(request.getParameter("name"));

        String error = serviceManager.updateService(serviceId, name,
                Validator.clean(request.getParameter("basePrice")),
                Validator.clean(request.getParameter("duration")),
                Validator.clean(request.getParameter("description")),
                Validator.clean(request.getParameter("extra")));

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("service", serviceManager.getServiceById(serviceId));
            request.getRequestDispatcher("/service/edit-service.jsp").forward(request, response);
            return;
        }
        SessionHelper.setFlash(request, "Service \"" + name + "\" was updated.");
        response.sendRedirect(request.getContextPath() + "/services");
    }
}
