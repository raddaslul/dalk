package com.dalk.domain.time;

import com.dalk.domain.ChatRoom;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConversion {

    public static String timeCreatedConversion(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String timeEndConversion(ChatRoom chatRoom) {
        LocalDateTime endAt;
        if(chatRoom.getTime()) {
            endAt = chatRoom.getCreatedAt().plusSeconds(40);
        } else {
            endAt = chatRoom.getCreatedAt().plusHours(1);
        }
        return endAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static Long restTime(LocalDateTime createdAt, Boolean time) {
        LocalDateTime currentTime = LocalDateTime.now();
        Long timeDiff = Duration.between(createdAt, currentTime).getSeconds(); //현재시간 계산
        Long resultConversion;
        if (time) {
            resultConversion = 40 - timeDiff;
            if((timeDiff) > 40){
                resultConversion = 0L;
            }
        } else {
            resultConversion = 3600 - timeDiff;
            if ((timeDiff) > 3600) {
                resultConversion = 0L;
            }
        }
        return resultConversion;
    }
}