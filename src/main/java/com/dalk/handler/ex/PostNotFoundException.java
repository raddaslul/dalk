package com.dalk.handler.ex;

import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message) {
        super(message);
    }
}
