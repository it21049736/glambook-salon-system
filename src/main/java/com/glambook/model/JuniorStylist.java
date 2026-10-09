package com.glambook.model;

/**
 * A junior stylist who is still building experience.
 * Earns a lower commission, limited to LKR 3000 per service.
 */
// INHERITANCE: JuniorStylist is a Stylist
public class JuniorStylist extends Stylist {

    public static final String LEVEL = "JUNIOR";
    private static final double COMMISSION_RATE = 0.10;
    private static final double MAX_COMMISSION = 3000;

    public JuniorStylist(String stylistId, String name, String phone, String email, String specialty,
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

    // METHOD OVERRIDING: 10% of the amount, but never more than LKR 3000
    @Override
    public double calculateCommission(double serviceAmount) {
        return Math.min(serviceAmount * COMMISSION_RATE, MAX_COMMISSION);
    }
}
