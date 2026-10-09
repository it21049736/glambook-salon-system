package com.glambook.service;

import com.glambook.dao.PaymentDAO;
import com.glambook.model.Appointment;
import com.glambook.model.CardPayment;
import com.glambook.model.CashPayment;
import com.glambook.model.Payment;
import com.glambook.model.User;
import com.glambook.util.IdGenerator;
import com.glambook.util.Validator;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Business logic for payments: create invoice (pay), view history, update status and delete voided records.
 */
public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final AppointmentService appointmentService = new AppointmentService();

    /**
     * Pays for an appointment and saves the invoice.
     * Returns the new payment id, or throws IllegalArgumentException with a message for the user.
     */
    public String pay(User user, String appointmentId, String method, String amountTendered,
                      String cardHolder, String cardNumber, String expiry, String cvv) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null || !appointmentService.canManage(user, appointment)) {
            throw new IllegalArgumentException("Appointment not found.");
        }
        if (!appointment.isActive()) {
            throw new IllegalArgumentException("This appointment is already paid or cancelled.");
        }

        String newId = IdGenerator.generateId(IdGenerator.PAYMENT, paymentDAO.getAllIds());
        double amount = appointment.getFinalPrice();
        Payment payment;

        if (CashPayment.METHOD.equals(method)) {
            if (!Validator.isPositiveNumber(amountTendered)) {
                throw new IllegalArgumentException("Please enter the cash amount received.");
            }
            payment = new CashPayment(newId, appointmentId, appointment.getCustomerId(), amount,
                    LocalDate.now(), Payment.PENDING, Double.parseDouble(amountTendered), 0);
        } else if (CardPayment.METHOD.equals(method)) {
            validateCard(cardHolder, cardNumber, expiry, cvv);
            CardPayment cardPayment = new CardPayment(newId, appointmentId, appointment.getCustomerId(), amount,
                    LocalDate.now(), Payment.PENDING, cardHolder, "");
            cardPayment.setCardNumber(cardNumber);
            payment = cardPayment;
        } else {
            throw new IllegalArgumentException("Please choose cash or card.");
        }

        // POLYMORPHISM: Java runs CashPayment.processPayment() or CardPayment.processPayment()
        if (!payment.processPayment()) {
            throw new IllegalArgumentException("Payment failed. The cash amount must cover LKR "
                    + String.format("%,.2f", amount) + ".");
        }
        if (!paymentDAO.add(payment)) {
            throw new IllegalArgumentException("Could not save the payment.");
        }
        // once it is paid the booking is closed
        appointmentService.markCompleted(appointmentId);
        return newId;
    }

    // UPDATE status (admin): PENDING, PAID or VOIDED
    public String updateStatus(String paymentId, String status) {
        Payment payment = paymentDAO.findById(paymentId);
        if (payment == null) {
            return "Payment not found.";
        }
        if (!Payment.PENDING.equals(status) && !Payment.PAID.equals(status) && !Payment.VOIDED.equals(status)) {
            return "Unknown status.";
        }
        payment.setStatus(status);
        return paymentDAO.update(payment) ? null : "Could not update the payment.";
    }

    // DELETE is only allowed for voided records, so real payments are never lost
    public String deleteVoided(String paymentId) {
        Payment payment = paymentDAO.findById(paymentId);
        if (payment == null) {
            return "Payment not found.";
        }
        if (!payment.isVoided()) {
            return "Only voided payments can be deleted. Void it first.";
        }
        return paymentDAO.delete(paymentId) ? null : "Could not delete the payment.";
    }

    public Payment getPaymentById(String paymentId) {
        return paymentDAO.findById(paymentId);
    }

    // customers see their own payments, admins see all (newest first)
    public List<Payment> getPaymentsFor(User user) {
        List<Payment> list = user.canAccessAdmin() ? paymentDAO.getAll() : paymentDAO.findByCustomer(user.getUserId());
        list.sort(Comparator.comparing(Payment::getPaymentDate).thenComparing(Payment::getPaymentId).reversed());
        return list;
    }

    public boolean canView(User user, Payment payment) {
        return user.canAccessAdmin() || payment.getCustomerId().equals(user.getUserId());
    }

    // total of all PAID payments, shown on the dashboard
    public double getTotalRevenue() {
        double total = 0;
        for (Payment payment : paymentDAO.getAll()) {
            if (Payment.PAID.equals(payment.getStatus())) {
                total += payment.getAmount();
            }
        }
        return total;
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAll();
    }

    private void validateCard(String holder, String number, String expiry, String cvv) {
        if (Validator.isEmpty(holder)) {
            throw new IllegalArgumentException("Please enter the card holder's name.");
        }
        String digits = number == null ? "" : number.replaceAll("\\s", "");
        if (!digits.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must have 16 digits.");
        }
        try {
            // expiry is MM/YY and must not be before this month
            YearMonth expiryMonth = YearMonth.parse(expiry, DateTimeFormatter.ofPattern("MM/yy"));
            if (expiryMonth.isBefore(YearMonth.now())) {
                throw new IllegalArgumentException("This card has expired.");
            }
        } catch (java.time.format.DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Expiry date must be in MM/YY format.");
        }
        if (cvv == null || !cvv.matches("\\d{3}")) {
            throw new IllegalArgumentException("CVV must have 3 digits.");
        }
    }
}
