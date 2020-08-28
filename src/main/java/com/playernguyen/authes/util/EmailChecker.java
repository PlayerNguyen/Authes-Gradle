package com.playernguyen.authes.util;

import java.util.regex.Pattern;

public class EmailChecker {

    public static boolean check(String plaintext) {
        if (plaintext == null) throw new NullPointerException("Plaintext cannot be null");

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        return pattern.matcher(plaintext).matches();
    }

}
