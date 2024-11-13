package com.team25.event.planner.core;

import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public class Validation {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    public static boolean isValidEmail(@Nullable String email){
        return email != null && Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public static boolean isValidPassword(@Nullable String password){
        return password != null && Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }
}
