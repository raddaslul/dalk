package com.dalk.exception.ex;

import lombok.Getter;

@Getter
public class ChatRoomNotFoundException extends RuntimeException{
    public ChatRoomNotFoundException(String message) {
        super(message);
    }
}
