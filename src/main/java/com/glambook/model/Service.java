package com.glambook.model;

/**
 * A treatment offered by the salon.
 * One line in services.txt:
 * serviceId|category|name|basePrice|durationMinutes|description|extra
 */
// ABSTRACTION: Service is abstract, a real service is always Hair, Skin or Makeup
public abstract class Service {

    public static final String HAIR = "HAIR";
    public static final String SKIN = "SKIN";
    public static final String MAKEUP = "MAKEUP";

    // ENCAPSULATION: private fields with getters and setters
    private String serviceId;
    private String name;
    private double basePrice;
    private int durationMinutes;
    private String description;

    public Service(String serviceId, String name, double basePrice, int durationMinutes, String description) {
        this.serviceId = serviceId;
        this.name = name;
        this.basePrice = basePrice;
        this.durationMinutes = durationMinutes;
        this.description = description;
    }

    // ABSTRACTION: each subclass gives its own category name
    public abstract String getCategory();

    // POLYMORPHISM: each type of service calculates its final price in a different way
    public abstract double calculatePrice();

    // the subclass specific value (hair length, product, occasion)
    public abstract String getExtraDetail();

    public abstract void setExtraDetail(String extraDetail);

    // label shown on the pages for the extra value
    public abstract String getExtraLabel();

    public String toFileString() {
        return serviceId + "|" + getCategory() + "|" + name + "|" + basePrice + "|"
                + durationMinutes + "|" + description + "|" + getExtraDetail();
    }

    // reads one line and creates the right subclass; null for a broken line
    public static Service fromFileString(String line) {
        try {
            String[] p = line.split("\\|", -1);
            if (p.length < 7) {
                return null;
            }
            return create(p[1].trim(), p[0], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4]), p[5], p[6]);
        } catch (Exception e) {
            return null;
        }
    }

    // small factory method: picks the subclass from the category text
    public static Service create(String category, String id, String name, double basePrice,
                                 int duration, String description, String extra) {
        switch (category) {
            case HAIR:
                return new HairService(id, name, basePrice, duration, description, extra);
            case SKIN:
                return new SkinService(id, name, basePrice, duration, description, extra);
            case MAKEUP:
                return new MakeupService(id, name, basePrice, duration, description, extra);
            default:
                return null;
        }
    }

    // ---------- getters and setters ----------

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
