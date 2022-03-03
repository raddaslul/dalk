package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);
    }
}