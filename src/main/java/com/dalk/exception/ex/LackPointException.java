package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class LackPointException extends RuntimeException{
    public LackPointException(String message) {
        super(message);
    }
}
