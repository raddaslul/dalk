package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(String message) {
        super(message);
    }
}
