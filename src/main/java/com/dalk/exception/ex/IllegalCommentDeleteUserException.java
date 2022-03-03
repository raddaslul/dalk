package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class IllegalCommentDeleteUserException extends RuntimeException {
    public IllegalCommentDeleteUserException(String message) {
        super(message);
    }
}