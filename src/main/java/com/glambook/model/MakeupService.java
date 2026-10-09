package com.glambook.model;

/**
 * Makeup services. Bridal makeup takes more work, so it costs 50% more.
 */
// INHERITANCE: MakeupService is a Service
public class MakeupService extends Service {

    public static final String CASUAL = "CASUAL";
    public static final String PARTY = "PARTY";
    public static final String BRIDAL = "BRIDAL";

    private String occasion;

    public MakeupService(String serviceId, String name, double basePrice, int durationMinutes,
                         String description, String occasion) {
        super(serviceId, name, basePrice, durationMinutes, description);
        setExtraDetail(occasion);
    }

    @Override
    public String getCategory() {
        return MAKEUP;
    }

    // METHOD OVERRIDING: bridal +50%, party +20%, casual is the base price
    @Override
    public double calculatePrice() {
        if (BRIDAL.equals(occasion)) {
            return getBasePrice() * 1.5;
        } else if (PARTY.equals(occasion)) {
            return getBasePrice() * 1.2;
        }
        return getBasePrice();
    }

    @Override
    public String getExtraDetail() {
        return occasion;
    }

    @Override
    public void setExtraDetail(String extraDetail) {
        if (BRIDAL.equalsIgnoreCase(extraDetail) || PARTY.equalsIgnoreCase(extraDetail)) {
            this.occasion = extraDetail.toUpperCase();
        } else {
            this.occasion = CASUAL;
        }
    }

    @Override
    public String getExtraLabel() {
        return "Occasion";
    }
}
