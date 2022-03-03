package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message) {
        super(message);
    }
}
