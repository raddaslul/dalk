package com.dalk.handler.ex;

import lombok.Getter;

@Getter
public class IllegalCommentUpdateUserException extends RuntimeException {
    public IllegalCommentUpdateUserException(String message) {
        super(message);
    }
}