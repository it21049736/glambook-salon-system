package com.glambook.model;

import java.time.LocalDate;

/**
 * Payment made with a debit or credit card.
 * Only the last 4 digits of the card are kept; the full number and CVV are never saved.
 */
// INHERITANCE: CardPayment is a Payment
public class CardPayment extends Payment {

    public static final String METHOD = "CARD";

    private String cardHolder;
    private String cardLast4;

    public CardPayment(String paymentId, String appointmentId, String customerId, double amount,
                       LocalDate paymentDate, String status, String cardHolder, String cardLast4) {
        super(paymentId, appointmentId, customerId, amount, paymentDate, status);
        this.cardHolder = cardHolder;
        this.cardLast4 = cardLast4;
    }

    // keeps only the last 4 digits of a full card number
    public void setCardNumber(String cardNumber) {
        String digits = cardNumber.replaceAll("\\D", "");
        this.cardLast4 = digits.length() >= 4 ? digits.substring(digits.length() - 4) : "";
    }

    @Override
    public String getMethod() {
        return METHOD;
    }

    // METHOD OVERRIDING: a card payment needs a holder name and a card number (4 digits stored)
    @Override
    public boolean processPayment() {
        if (cardHolder == null || cardHolder.trim().isEmpty() || cardLast4 == null || cardLast4.length() != 4) {
            return false;
        }
        setStatus(PAID);
        return true;
    }

    @Override
    public String getDetails() {
        return "Card - " + cardHolder + ", **** **** **** " + cardLast4;
    }

    @Override
    protected String getExtra1() {
        return cardHolder;
    }

    @Override
    protected String getExtra2() {
        return cardLast4;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardLast4() {
        return cardLast4;
    }
}
