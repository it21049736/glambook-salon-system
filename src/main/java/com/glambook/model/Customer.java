package com.glambook.model;

/**
 * A salon customer. Customers can be REGULAR or PREMIUM members.
 * Premium members get a discount when they book (see the DiscountPolicy classes).
 */
// INHERITANCE: Customer is a User and reuses all the common fields
public class Customer extends User {

    public static final String ROLE = "CUSTOMER";
    public static final String REGULAR = "REGULAR";
    public static final String PREMIUM = "PREMIUM";

    private String membership;

    public Customer(String userId, String fullName, String email, String phone,
                    String password, String membership) {
        // calls the User constructor
        super(userId, fullName, email, phone, password);
        setMembership(membership);
    }

    // METHOD OVERRIDING: customer role
    @Override
    public String getRole() {
        return ROLE;
    }

    // METHOD OVERRIDING: customers can never open admin pages
    @Override
    public boolean canAccessAdmin() {
        return false;
    }

    // METHOD OVERRIDING: customers go to their appointments after login
    @Override
    public String getHomePage() {
        return "/appointments/my";
    }

    @Override
    protected String getExtraField() {
        return membership;
    }

    public boolean isPremium() {
        return PREMIUM.equals(membership);
    }

    public String getMembership() {
        return membership;
    }

    // anything that is not PREMIUM is stored as REGULAR
    public void setMembership(String membership) {
        this.membership = PREMIUM.equalsIgnoreCase(membership) ? PREMIUM : REGULAR;
    }
}
