package com.glambook.util;

import java.util.List;

/**
 * Creates the next ID for a record, e.g. C001 -> C002.
 * It finds the highest number already used with the same prefix and adds one.
 */
public class IdGenerator {

    public static final String CUSTOMER = "C";
    public static final String ADMIN = "AD";
    public static final String SERVICE = "S";
    public static final String STYLIST = "ST";
    public static final String APPOINTMENT = "A";
    public static final String PAYMENT = "P";
    public static final String REVIEW = "R";

    private IdGenerator() {
    }

    public static String generateId(String prefix, List<String> existingIds) {
        int max = 0;
        for (String id : existingIds) {
            if (id != null && id.startsWith(prefix)) {
                try {
                    int number = Integer.parseInt(id.substring(prefix.length()));
                    if (number > max) {
                        max = number;
                    }
                } catch (NumberFormatException e) {
                    // not this prefix (e.g. "ST001" when looking for "S"), so skip it
                }
            }
        }
        // %03d pads the number with zeros: 1 -> 001
        return prefix + String.format("%03d", max + 1);
    }
}
