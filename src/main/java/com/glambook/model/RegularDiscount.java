package com.glambook.model;

/**
 * Regular customers pay the normal price.
 */
// POLYMORPHISM: implements DiscountPolicy in its own way
public class RegularDiscount implements DiscountPolicy {

    @Override
    public double calculateDiscount(double amount) {
        return 0;
    }

    @Override
    public String getDescription() {
        return "No discount (Regular member)";
    }
}
