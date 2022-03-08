package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class DuplicateVoteException extends RuntimeException{
    public DuplicateVoteException(String message) {
        super(message);
    }
}
