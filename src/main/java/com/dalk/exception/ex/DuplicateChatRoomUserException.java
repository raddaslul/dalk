package com.dalk.exception.ex;

public class DuplicateChatRoomUserException extends RuntimeException {
    public DuplicateChatRoomUserException(String message) {
        super(message);
    }
}
