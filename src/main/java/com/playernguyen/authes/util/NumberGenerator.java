package com.playernguyen.authes.util;

public class NumberGenerator {

    private final static String PATTERN = "1234567890";

    public static String generate(int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int pos = (int) Math.floor(Math.random() * PATTERN.length());
            builder.append(PATTERN.charAt(pos));
        }
        return builder.toString().trim();
    }

}
