package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class PasswordNotEqualException extends RuntimeException {
    public PasswordNotEqualException(String message) {
        super(message);
    }
}