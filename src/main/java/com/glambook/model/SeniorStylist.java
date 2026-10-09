package com.glambook.model;

/**
 * An experienced stylist. Earns a higher commission on every service.
 */
// INHERITANCE: SeniorStylist is a Stylist
public class SeniorStylist extends Stylist {

    public static final String LEVEL = "SENIOR";
    private static final double COMMISSION_RATE = 0.20;

    public SeniorStylist(String stylistId, String name, String phone, String email, String specialty,
                         String workingDays, String shiftStart, String shiftEnd, int experienceYears) {
        super(stylistId, name, phone, email, specialty, workingDays, shiftStart, shiftEnd, experienceYears);
    }

    @Override
    public String getLevel() {
        return LEVEL;
    }

    @Override
    public double getCommissionRate() {
        return COMMISSION_RATE;
    }

    // METHOD OVERRIDING: senior stylists get 20% of the service amount
    @Override
    public double calculateCommission(double serviceAmount) {
        return serviceAmount * COMMISSION_RATE;
    }
}
