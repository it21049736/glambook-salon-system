package com.glambook.servlet.admin;

import com.glambook.model.Appointment;
import com.glambook.model.Customer;
import com.glambook.model.Payment;
import com.glambook.service.AppointmentService;
import com.glambook.service.PaymentService;
import com.glambook.service.ServiceManager;
import com.glambook.service.StylistService;
import com.glambook.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Admin dashboard with a quick summary of the salon.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ServiceManager serviceManager = new ServiceManager();
    private final StylistService stylistService = new StylistService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // user statistics
        List<Customer> customers = userService.getAllCustomers();
        int premiumCount = 0;
        for (Customer customer : customers) {
            if (customer.isPremium()) {
                premiumCount++;
            }
        }
        request.setAttribute("customerCount", customers.size());
        request.setAttribute("premiumCount", premiumCount);
        request.setAttribute("adminCount", userService.getAllUsers().size() - customers.size());

        // service statistics
        request.setAttribute("serviceCount", serviceManager.getAllServices().size());
        request.setAttribute("stylistCount", stylistService.getAllStylists().size());

        // appointment statistics: upcoming = still active and not in the past
        int upcoming = 0;
        int todayCount = 0;
        for (Appointment a : appointmentService.getAllAppointments()) {
            if (a.isActive() && !a.getDate().isBefore(LocalDate.now())) {
                upcoming++;
                if (a.getDate().equals(LocalDate.now())) {
                    todayCount++;
                }
            }
        }
        request.setAttribute("upcomingCount", upcoming);
        request.setAttribute("todayCount", todayCount);

        // payment statistics
        int pendingPayments = 0;
        for (Payment p : paymentService.getAllPayments()) {
            if (Payment.PENDING.equals(p.getStatus())) {
                pendingPayments++;
            }
        }
        request.setAttribute("totalRevenue", paymentService.getTotalRevenue());
        request.setAttribute("pendingPayments", pendingPayments);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
