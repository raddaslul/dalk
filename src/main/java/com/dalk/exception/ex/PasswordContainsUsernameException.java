package com.dalk.exception.ex;

public class PasswordContainsUsernameException extends RuntimeException {
    public PasswordContainsUsernameException(String message) {
        super(message);
    }
}
