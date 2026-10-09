package com.glambook.model;

/**
 * A rule that decides how much discount a customer gets on a booking.
 */
// ABSTRACTION: interface implemented by RegularDiscount and PremiumDiscount
public interface DiscountPolicy {

    // returns the discount amount (not the final price)
    double calculateDiscount(double amount);

    String getDescription();
}
