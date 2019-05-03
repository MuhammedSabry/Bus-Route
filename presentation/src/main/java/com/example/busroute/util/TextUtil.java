package com.example.busroute.util;

import android.text.Editable;

public class TextUtil {

    public static String getText(Editable editable) {
        return editable == null ? "" : editable.toString().trim();
    }

    public static int integerOf(String str) {
        return Integer.valueOf(str);
    }

    public static double doubleOf(String str) {
        return Double.valueOf(str);
    }
}
