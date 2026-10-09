package com.glambook.servlet.stylist;

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
 * Admin registers a new stylist. GET shows the form, POST saves it.
 */
@WebServlet("/admin/stylists/add")
public class AddStylistServlet extends HttpServlet {

    private final StylistService stylistService = new StylistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/stylist/add-stylist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = Validator.clean(request.getParameter("name"));
        String workingDays = readWorkingDays(request);

        String error = stylistService.addStylist(
                Validator.clean(request.getParameter("level")),
                name,
                Validator.clean(request.getParameter("phone")),
                Validator.clean(request.getParameter("email")),
                Validator.clean(request.getParameter("specialty")),
                workingDays,
                Validator.clean(request.getParameter("shiftStart")),
                Validator.clean(request.getParameter("shiftEnd")),
                Validator.clean(request.getParameter("experienceYears")));

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/stylist/add-stylist.jsp").forward(request, response);
            return;
        }
        SessionHelper.setFlash(request, "Stylist " + name + " was added.");
        response.sendRedirect(request.getContextPath() + "/stylists");
    }

    // the ticked day checkboxes come as an array, e.g. [MON, TUE] -> "MON,TUE"
    static String readWorkingDays(HttpServletRequest request) {
        String[] days = request.getParameterValues("workingDays");
        return days == null ? "" : String.join(",", days);
    }
}
