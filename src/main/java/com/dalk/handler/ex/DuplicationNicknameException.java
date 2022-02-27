package com.dalk.handler.ex;

import lombok.Getter;

@Getter
public class DuplicationNicknameException extends RuntimeException{
    public DuplicationNicknameException(String message) {
        super(message);
    }
}
