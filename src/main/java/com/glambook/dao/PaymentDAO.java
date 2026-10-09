package com.glambook.dao;

import com.glambook.model.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes payments in payments.txt.
 */
public class PaymentDAO {

    private static final String FILE_NAME = "payments.txt";

    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        for (String line : FileHandler.readLines(FILE_NAME)) {
            Payment payment = Payment.fromFileString(line);
            if (payment != null) {
                payments.add(payment);
            }
        }
        return payments;
    }

    public Payment findById(String paymentId) {
        for (Payment payment : getAll()) {
            if (payment.getPaymentId().equals(paymentId)) {
                return payment;
            }
        }
        return null;
    }

    public List<Payment> findByCustomer(String customerId) {
        List<Payment> result = new ArrayList<>();
        for (Payment payment : getAll()) {
            if (payment.getCustomerId().equals(customerId)) {
                result.add(payment);
            }
        }
        return result;
    }

    public List<String> getAllIds() {
        List<String> ids = new ArrayList<>();
        for (Payment payment : getAll()) {
            ids.add(payment.getPaymentId());
        }
        return ids;
    }

    // CREATE
    public boolean add(Payment payment) {
        return FileHandler.appendLine(FILE_NAME, payment.toFileString());
    }

    // UPDATE (used to change the status)
    public boolean update(Payment updated) {
        List<Payment> payments = getAll();
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId().equals(updated.getPaymentId())) {
                payments.set(i, updated);
                return saveAll(payments);
            }
        }
        return false;
    }

    // DELETE
    public boolean delete(String paymentId) {
        List<Payment> payments = getAll();
        boolean removed = payments.removeIf(p -> p.getPaymentId().equals(paymentId));
        return removed && saveAll(payments);
    }

    private boolean saveAll(List<Payment> payments) {
        List<String> lines = new ArrayList<>();
        for (Payment payment : payments) {
            lines.add(payment.toFileString());
        }
        return FileHandler.writeLines(FILE_NAME, lines);
    }
}
