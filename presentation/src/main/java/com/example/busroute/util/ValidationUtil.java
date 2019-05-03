package com.example.busroute.util;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean isValidText(String input) {
        return !TextUtils.isEmpty(input);
    }

    public static boolean isValidInt(String input, int min, int max) {
        try {
            int number = Integer.valueOf(input);
            return number >= min && number <= max;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isValidDouble(String input, double min, double max) {
        try {
            double number = Double.valueOf(input);
            return number >= min && number <= max;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 8 && !password.contains(" ") && password.length() <= 15;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidName(String name) {
        Pattern name_pattern = Pattern.compile("[a-zA-Z\\s]+");
        return !TextUtils.isEmpty(name) && name_pattern.matcher(name).matches();
    }

}
