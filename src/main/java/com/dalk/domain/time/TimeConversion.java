package com.dalk.domain.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConversion {

    public static String timeCreatedConversion(LocalDateTime createdAt) {
        String resultConversion = "";
        resultConversion = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return resultConversion;
    }

    public static Long restTime(LocalDateTime createdAt, Boolean time) {
        LocalDateTime currentTime = LocalDateTime.now();
        Long timeDiff = Duration.between(createdAt, currentTime).getSeconds(); //현재시간 계산
        Long resultConversion;
        if (time) {
            resultConversion = 30 - timeDiff;
            if((timeDiff) > 30){
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

    public static String timePostConversion(LocalDateTime createdAt, Boolean time) {
        LocalDateTime currentTime = LocalDateTime.now();

        Long timeDiff = Duration.between(createdAt, currentTime).getSeconds(); //현재시간 계산
        String resultConversion = "";
        Long limitTime;
        if(time){
            if ((timeDiff / 86400) > 1) {
                resultConversion = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            else if ((timeDiff / 60) > 0) { // 분
                resultConversion = 20 - (timeDiff / 60) + "분 남았습니다";
                if((timeDiff / 60) > 20){
                    resultConversion = "0분 남았습니다";
                }
            }else{
                resultConversion =  "방금 생성됐습니다";
            }
        } else{
            if ((timeDiff / 86400) > 1) {
                resultConversion = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            else if ((timeDiff / 60) > 0) { // 분
                resultConversion = 60 - (timeDiff / 60) + "분 남았습니다";
                if((timeDiff / 60) > 60){
                    resultConversion = "0분 남았습니다";
                }
            } else {
                resultConversion =  "방금 생성됐습니다";
            }
        }



        return resultConversion;
    }

    public static String timeChatConversion(LocalDateTime createdAt) {
        LocalDateTime currentTime = LocalDateTime.now();
        String resultConversion = "";

        if (currentTime.getDayOfMonth() > createdAt.getDayOfMonth()) {
            resultConversion = createdAt.format(DateTimeFormatter.ofPattern("MM월 dd일"));
        }
        else {
            resultConversion = createdAt.format(DateTimeFormatter.ofPattern("a hh시 mm분 ss초"));
        }

        return resultConversion;
    }
}