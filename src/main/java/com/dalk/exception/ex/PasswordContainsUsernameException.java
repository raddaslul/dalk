package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class PasswordContainsUsernameException extends RuntimeException {
    public PasswordContainsUsernameException(String message) {
        super(message);
    }
}
