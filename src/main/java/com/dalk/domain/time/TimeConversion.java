package com.dalk.domain.time;

import com.dalk.domain.ChatRoom;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConversion {

    public static String timeCreatedConversion(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String timeEndConversion(ChatRoom chatRoom) {
        LocalDateTime endAt;
        if(chatRoom.getTime()) {
            endAt = chatRoom.getCreatedAt().plusMinutes(20);
        } else {
            endAt = chatRoom.getCreatedAt().plusHours(1);
        }
        return endAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}