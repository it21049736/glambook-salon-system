package com.glambook.servlet.service;

import com.glambook.service.ServiceManager;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin deletes a service (POST only, the page asks for confirmation first).
 */
@WebServlet("/admin/services/delete")
public class DeleteServiceServlet extends HttpServlet {

    private final ServiceManager serviceManager = new ServiceManager();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String serviceId = request.getParameter("serviceId");
        if (serviceManager.deleteService(serviceId)) {
            SessionHelper.setFlash(request, "Service " + serviceId + " was deleted.");
        } else {
            SessionHelper.setFlashError(request, "Service not found.");
        }
        response.sendRedirect(request.getContextPath() + "/services");
    }
}
