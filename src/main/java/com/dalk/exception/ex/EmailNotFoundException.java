package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(String message) {
        super(message);
    }
}