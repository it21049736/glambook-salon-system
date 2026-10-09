package com.glambook.model;

/**
 * Base class for every person who can log in to GlamBook.
 * One line in users.txt:
 * userId|role|fullName|email|phone|password|extra
 */
// ABSTRACTION: User is abstract, so we only ever create a Customer or an AdminUser
public abstract class User {

    // ENCAPSULATION: fields are private and only changed through getters and setters
    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private String password;

    public User(String userId, String fullName, String email, String phone, String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // ABSTRACTION: every subclass must say which role it has
    public abstract String getRole();

    // POLYMORPHISM: role-based authentication, each subclass decides if it can open admin pages
    public abstract boolean canAccessAdmin();

    // POLYMORPHISM: the page a user is sent to after login is different for each role
    public abstract String getHomePage();

    // the last column in the file (membership for customers, position for admins)
    protected abstract String getExtraField();

    // shared login check for all user types
    public boolean checkPassword(String input) {
        return password != null && password.equals(input);
    }

    // converts the object into one line for users.txt
    public String toFileString() {
        return userId + "|" + getRole() + "|" + fullName + "|" + email + "|"
                + phone + "|" + password + "|" + getExtraField();
    }

    // reads one line and creates the correct subclass; returns null if the line is broken
    public static User fromFileString(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 7) {
                return null;
            }
            String role = parts[1].trim();
            if (AdminUser.ROLE.equals(role)) {
                return new AdminUser(parts[0], parts[2], parts[3], parts[4], parts[5], parts[6]);
            } else if (Customer.ROLE.equals(role)) {
                return new Customer(parts[0], parts[2], parts[3], parts[4], parts[5], parts[6]);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // ---------- getters and setters ----------

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
