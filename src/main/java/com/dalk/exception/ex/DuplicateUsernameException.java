package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}