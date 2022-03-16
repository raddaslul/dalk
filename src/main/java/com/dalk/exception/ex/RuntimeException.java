package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class RuntimeException extends java.lang.RuntimeException {
    public RuntimeException(String message) {
        super(message);
    }
}
