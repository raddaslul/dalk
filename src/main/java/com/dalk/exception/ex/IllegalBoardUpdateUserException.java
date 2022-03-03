package com.dalk.exception.ex;

public class IllegalBoardUpdateUserException extends RuntimeException{
    public IllegalBoardUpdateUserException(String message) {
        super(message);
    }
}
