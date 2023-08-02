package com.topjava.votesystem.util;

public class NumberUtil {
        public static String formatDecimal(Float number) {
            if (number != null) {
                return String.format("%.2f", number);
            } else {
                return "0.00";
            }
        }
}
