package com.glambook.servlet.stylist;

import com.glambook.service.StylistService;
import com.glambook.servlet.common.SessionHelper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin removes a stylist (POST only).
 */
@WebServlet("/admin/stylists/delete")
public class DeleteStylistServlet extends HttpServlet {

    private final StylistService stylistService = new StylistService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String stylistId = request.getParameter("stylistId");
        if (stylistService.deleteStylist(stylistId)) {
            SessionHelper.setFlash(request, "Stylist " + stylistId + " was removed.");
        } else {
            SessionHelper.setFlashError(request, "Stylist not found.");
        }
        response.sendRedirect(request.getContextPath() + "/stylists");
    }
}
