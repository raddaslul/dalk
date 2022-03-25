package com.dalk.exception.ex;

public class PasswordNotEqualException extends RuntimeException {
    public PasswordNotEqualException(String message) {
        super(message);
    }
}