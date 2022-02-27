package com.dalk.handler.ex;

import lombok.Getter;

@Getter
public class PasswordContainsUsernameException extends RuntimeException {
    public PasswordContainsUsernameException(String message) {
        super(message);
    }
}
