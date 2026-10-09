package com.glambook.model;

import java.time.LocalDate;

/**
 * Payment made with cash at the counter.
 */
// INHERITANCE: CashPayment is a Payment
public class CashPayment extends Payment {

    public static final String METHOD = "CASH";

    private double amountTendered;
    private double changeGiven;

    public CashPayment(String paymentId, String appointmentId, String customerId, double amount,
                       LocalDate paymentDate, String status, double amountTendered, double changeGiven) {
        super(paymentId, appointmentId, customerId, amount, paymentDate, status);
        this.amountTendered = amountTendered;
        this.changeGiven = changeGiven;
    }

    @Override
    public String getMethod() {
        return METHOD;
    }

    // METHOD OVERRIDING: cash is accepted only if the customer gave enough money; the change is worked out
    @Override
    public boolean processPayment() {
        if (amountTendered < getAmount()) {
            return false;
        }
        changeGiven = amountTendered - getAmount();
        setStatus(PAID);
        return true;
    }

    @Override
    public String getDetails() {
        return String.format("Cash - tendered LKR %,.2f, change LKR %,.2f", amountTendered, changeGiven);
    }

    @Override
    protected String getExtra1() {
        return String.valueOf(amountTendered);
    }

    @Override
    protected String getExtra2() {
        return String.valueOf(changeGiven);
    }

    public double getAmountTendered() {
        return amountTendered;
    }

    public void setAmountTendered(double amountTendered) {
        this.amountTendered = amountTendered;
    }

    public double getChangeGiven() {
        return changeGiven;
    }
}
