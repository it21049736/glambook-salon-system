package com.glambook.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Server-side validation helpers shared by all services.
 */
public class Validator {

    public static final int MIN_PASSWORD_LENGTH = 6;

    // something@something.something
    private static final String EMAIL_REGEX = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$";
    // Sri Lankan phone number: 10 digits starting with 0, e.g. 0771234567
    private static final String PHONE_REGEX = "^0\\d{9}$";

    private Validator() {
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && email.trim().matches(EMAIL_REGEX);
    }

    public static boolean isValidPhone(String phone) {
        return !isEmpty(phone) && phone.trim().matches(PHONE_REGEX);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static boolean isPositiveNumber(String value) {
        try {
            return Double.parseDouble(value.trim()) > 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isPositiveInteger(String value) {
        try {
            return Integer.parseInt(value.trim()) > 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    // checks the yyyy-MM-dd format used by <input type="date">
    public static boolean isValidDate(String value) {
        try {
            LocalDate.parse(value.trim());
            return true;
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    // today is allowed, yesterday is not
    public static boolean isNotPastDate(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.now());
    }

    /**
     * Cleans a form value before it is stored: null becomes "", spaces are trimmed
     * and the | character is replaced because | separates fields in our files.
     */
    public static String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.trim()
                .replace("|", "/")
                .replace("\r", " ")
                .replace("\n", " ");
    }
}
