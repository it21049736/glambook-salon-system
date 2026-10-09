package com.glambook.service;

import com.glambook.dao.UserDAO;
import com.glambook.model.Customer;
import com.glambook.model.User;
import com.glambook.util.IdGenerator;
import com.glambook.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic for user management: register, login, search, update and delete.
 * Methods that change data return an error message, or null when everything worked.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    // REGISTER a new customer account
    public String register(String fullName, String email, String phone, String password,
                           String confirmPassword, String membership) {
        String error = validateDetails(fullName, email, phone);
        if (error != null) {
            return error;
        }
        if (!Validator.isValidPassword(password)) {
            return "Password must have at least " + Validator.MIN_PASSWORD_LENGTH + " characters.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        if (userDAO.findByEmail(email) != null) {
            return "An account with this email already exists.";
        }

        String newId = IdGenerator.generateId(IdGenerator.CUSTOMER, userDAO.getAllIds());
        Customer customer = new Customer(newId, fullName, email.toLowerCase(), phone, password, membership);
        return userDAO.add(customer) ? null : "Could not save the account. Please try again.";
    }

    // LOGIN: returns the user if email and password match, otherwise null
    public User login(String email, String password) {
        if (Validator.isEmpty(email) || Validator.isEmpty(password)) {
            return null;
        }
        User user = userDAO.findByEmail(email.trim());
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }

    // UPDATE profile; the password is only changed if a new one is typed
    public String updateProfile(String userId, String fullName, String email, String phone,
                                String newPassword, String membership) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return "User not found.";
        }
        String error = validateDetails(fullName, email, phone);
        if (error != null) {
            return error;
        }
        // the new email must not belong to another account
        User sameEmail = userDAO.findByEmail(email);
        if (sameEmail != null && !sameEmail.getUserId().equals(userId)) {
            return "This email is already used by another account.";
        }
        if (!Validator.isEmpty(newPassword)) {
            if (!Validator.isValidPassword(newPassword)) {
                return "New password must have at least " + Validator.MIN_PASSWORD_LENGTH + " characters.";
            }
            user.setPassword(newPassword);
        }

        user.setFullName(fullName);
        user.setEmail(email.toLowerCase());
        user.setPhone(phone);
        // only customers have a membership type
        if (user instanceof Customer && !Validator.isEmpty(membership)) {
            ((Customer) user).setMembership(membership);
        }
        return userDAO.update(user) ? null : "Could not update the profile.";
    }

    // DELETE an account
    public boolean deleteUser(String userId) {
        return userDAO.delete(userId);
    }

    // SEARCH by id, name, email or phone (empty keyword returns everyone)
    public List<User> searchUsers(String keyword) {
        List<User> all = userDAO.getAll();
        if (Validator.isEmpty(keyword)) {
            return all;
        }
        String key = keyword.trim().toLowerCase();
        List<User> result = new ArrayList<>();
        for (User user : all) {
            if (user.getUserId().toLowerCase().contains(key)
                    || user.getFullName().toLowerCase().contains(key)
                    || user.getEmail().toLowerCase().contains(key)
                    || user.getPhone().contains(key)) {
                result.add(user);
            }
        }
        return result;
    }

    public User getUserById(String userId) {
        return userDAO.findById(userId);
    }

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (User user : userDAO.getAll()) {
            if (user instanceof Customer) {
                customers.add((Customer) user);
            }
        }
        return customers;
    }

    // id -> name map, used to show customer names on appointment and payment pages
    public Map<String, String> getUserNames() {
        Map<String, String> names = new HashMap<>();
        for (User user : userDAO.getAll()) {
            names.put(user.getUserId(), user.getFullName());
        }
        return names;
    }

    // checks shared by register and update
    private String validateDetails(String fullName, String email, String phone) {
        if (Validator.isEmpty(fullName) || Validator.isEmpty(email) || Validator.isEmpty(phone)) {
            return "Please fill in all required fields.";
        }
        if (!Validator.isValidEmail(email)) {
            return "Please enter a valid email address.";
        }
        if (!Validator.isValidPhone(phone)) {
            return "Phone number must have 10 digits and start with 0 (e.g. 0771234567).";
        }
        return null;
    }
}
