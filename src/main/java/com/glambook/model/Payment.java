package com.glambook.model;

import java.time.LocalDate;

/**
 * A payment (invoice) for one appointment.
 * One line in payments.txt:
 * paymentId|method|appointmentId|customerId|amount|paymentDate|status|extra1|extra2
 * CASH: extra1 = amount tendered, extra2 = change given
 * CARD: extra1 = card holder,     extra2 = last 4 digits of the card
 */
// ABSTRACTION: Payment is abstract, a real payment is either CashPayment or CardPayment
public abstract class Payment {

    public static final String PENDING = "PENDING";
    public static final String PAID = "PAID";
    public static final String VOIDED = "VOIDED";

    // ENCAPSULATION: private fields with getters and setters
    private String paymentId;
    private String appointmentId;
    private String customerId;
    private double amount;
    private LocalDate paymentDate;
    private String status;

    public Payment(String paymentId, String appointmentId, String customerId, double amount,
                   LocalDate paymentDate, String status) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        setStatus(status);
    }

    // ABSTRACTION: CASH or CARD
    public abstract String getMethod();

    // POLYMORPHISM: each payment type is processed in its own way; returns true if it succeeded
    public abstract boolean processPayment();

    // short text for the invoice, e.g. "Cash: tendered LKR 5000, change LKR 2000"
    public abstract String getDetails();

    protected abstract String getExtra1();

    protected abstract String getExtra2();

    public boolean isVoided() {
        return VOIDED.equals(status);
    }

    public String toFileString() {
        return paymentId + "|" + getMethod() + "|" + appointmentId + "|" + customerId + "|" + amount + "|"
                + paymentDate + "|" + status + "|" + getExtra1() + "|" + getExtra2();
    }

    // reads one line and creates the right subclass; null for a broken line
    public static Payment fromFileString(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 9) {
                return null;
            }
            double amount = Double.parseDouble(p[4]);
            LocalDate date = LocalDate.parse(p[5]);
            if (CashPayment.METHOD.equals(p[1])) {
                return new CashPayment(p[0], p[2], p[3], amount, date, p[6],
                        Double.parseDouble(p[7]), Double.parseDouble(p[8]));
            } else if (CardPayment.METHOD.equals(p[1])) {
                return new CardPayment(p[0], p[2], p[3], amount, date, p[6], p[7], p[8]);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // ---------- getters and setters ----------

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    // only PENDING, PAID or VOIDED are allowed
    public void setStatus(String status) {
        if (PAID.equals(status) || VOIDED.equals(status)) {
            this.status = status;
        } else {
            this.status = PENDING;
        }
    }
}
