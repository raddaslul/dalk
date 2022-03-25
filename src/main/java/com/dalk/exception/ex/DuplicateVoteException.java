package com.dalk.exception.ex;

public class DuplicateVoteException extends RuntimeException{
    public DuplicateVoteException(String message) {
        super(message);
    }
}
