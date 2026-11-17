package org.example.util;

import java.util.Locale;

public class Money {

    public static String format(double value) {
        return String.format(Locale.of("pt", "BR"), "R$ %.2f", value);
    }
}
