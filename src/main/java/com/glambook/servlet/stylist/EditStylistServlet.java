package com.glambook.servlet.stylist;

import com.glambook.model.Stylist;
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
 * Admin edits a stylist's profile and schedule. GET ?id=ST001 shows the form, POST saves it.
 */
@WebServlet("/admin/stylists/edit")
public class EditStylistServlet extends HttpServlet {

    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Stylist stylist = stylistService.getStylistById(request.getParameter("id"));
        if (stylist == null) {
            SessionHelper.setFlashError(request, "Stylist not found.");
            response.sendRedirect(request.getContextPath() + "/stylists");
            return;
        }
        request.setAttribute("stylist", stylist);
        request.getRequestDispatcher("/stylist/edit-stylist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String stylistId = Validator.clean(request.getParameter("stylistId"));
        String name = Validator.clean(request.getParameter("name"));

        String error = stylistService.updateStylist(
                stylistId,
                Validator.clean(request.getParameter("level")),
                name,
                Validator.clean(request.getParameter("phone")),
                Validator.clean(request.getParameter("email")),
                Validator.clean(request.getParameter("specialty")),
                AddStylistServlet.readWorkingDays(request),
                Validator.clean(request.getParameter("shiftStart")),
                Validator.clean(request.getParameter("shiftEnd")),
                Validator.clean(request.getParameter("experienceYears")));

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("stylist", stylistService.getStylistById(stylistId));
            request.getRequestDispatcher("/stylist/edit-stylist.jsp").forward(request, response);
            return;
        }
        SessionHelper.setFlash(request, "Stylist " + name + " was updated.");
        response.sendRedirect(request.getContextPath() + "/stylists");
    }
}
