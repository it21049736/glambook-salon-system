package com.glambook.model;

/**
 * Premium members get 10% off every booking.
 */
// POLYMORPHISM: implements DiscountPolicy in its own way
public class PremiumDiscount implements DiscountPolicy {

    private static final double RATE = 0.10;

    @Override
    public double calculateDiscount(double amount) {
        return amount * RATE;
    }

    @Override
    public String getDescription() {
        return "10% Premium member discount";
    }
}
