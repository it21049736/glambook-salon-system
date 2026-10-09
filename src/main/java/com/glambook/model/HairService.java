package com.glambook.model;

/**
 * Hair services. Longer hair needs more time and product, so it costs more.
 */
// INHERITANCE: HairService is a Service
public class HairService extends Service {

    public static final String SHORT = "SHORT";
    public static final String MEDIUM = "MEDIUM";
    public static final String LONG = "LONG";

    private String hairLength;

    public HairService(String serviceId, String name, double basePrice, int durationMinutes,
                       String description, String hairLength) {
        super(serviceId, name, basePrice, durationMinutes, description);
        setExtraDetail(hairLength);
    }

    @Override
    public String getCategory() {
        return HAIR;
    }

    // METHOD OVERRIDING: medium hair adds LKR 500, long hair adds LKR 1000
    @Override
    public double calculatePrice() {
        if (LONG.equals(hairLength)) {
            return getBasePrice() + 1000;
        } else if (MEDIUM.equals(hairLength)) {
            return getBasePrice() + 500;
        }
        return getBasePrice();
    }

    @Override
    public String getExtraDetail() {
        return hairLength;
    }

    // only SHORT, MEDIUM or LONG are allowed; anything else becomes SHORT
    @Override
    public void setExtraDetail(String extraDetail) {
        if (MEDIUM.equalsIgnoreCase(extraDetail) || LONG.equalsIgnoreCase(extraDetail)) {
            this.hairLength = extraDetail.toUpperCase();
        } else {
            this.hairLength = SHORT;
        }
    }

    @Override
    public String getExtraLabel() {
        return "Hair length";
    }
}
