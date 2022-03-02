package com.dalk.handler.ex;

import lombok.Getter;

@Getter
public class PasswordNotCollectException extends RuntimeException {
    public PasswordNotCollectException(String message) {
        super(message);
    }
}