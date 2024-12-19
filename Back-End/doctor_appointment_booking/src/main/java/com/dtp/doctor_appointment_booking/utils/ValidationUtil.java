package com.dtp.doctor_appointment_booking.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String NAME_REGEX = "^[a-zA-Z ]+$";

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(NAME_REGEX, name);
    }
}
