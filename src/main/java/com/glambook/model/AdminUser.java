package com.glambook.model;

/**
 * A salon staff member who manages the system (users, services, stylists, payments, reviews).
 */
// INHERITANCE: AdminUser is a User with an extra "position" field
public class AdminUser extends User {

    public static final String ROLE = "ADMIN";

    private String position;

    public AdminUser(String userId, String fullName, String email, String phone,
                     String password, String position) {
        super(userId, fullName, email, phone, password);
        this.position = position;
    }

    // METHOD OVERRIDING: admin role
    @Override
    public String getRole() {
        return ROLE;
    }

    // METHOD OVERRIDING: admins are allowed into the /admin pages
    @Override
    public boolean canAccessAdmin() {
        return true;
    }

    // METHOD OVERRIDING: admins go to the dashboard after login
    @Override
    public String getHomePage() {
        return "/admin/dashboard";
    }

    @Override
    protected String getExtraField() {
        return position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
