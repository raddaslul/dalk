package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class BoardNotFoundException extends RuntimeException{
    public BoardNotFoundException(String message) {
        super(message);
    }
}
