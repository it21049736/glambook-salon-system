package com.glambook.model;

/**
 * Skin care services such as facials. A product charge is added to the base price.
 */
// INHERITANCE: SkinService is a Service
public class SkinService extends Service {

    // 10% extra for the skin care products used in the treatment
    public static final double PRODUCT_CHARGE_RATE = 0.10;

    private String productBrand;

    public SkinService(String serviceId, String name, double basePrice, int durationMinutes,
                       String description, String productBrand) {
        super(serviceId, name, basePrice, durationMinutes, description);
        this.productBrand = productBrand;
    }

    @Override
    public String getCategory() {
        return SKIN;
    }

    // METHOD OVERRIDING: base price plus the product charge
    @Override
    public double calculatePrice() {
        return getBasePrice() + (getBasePrice() * PRODUCT_CHARGE_RATE);
    }

    @Override
    public String getExtraDetail() {
        return productBrand;
    }

    @Override
    public void setExtraDetail(String extraDetail) {
        this.productBrand = extraDetail;
    }

    @Override
    public String getExtraLabel() {
        return "Product brand";
    }
}
