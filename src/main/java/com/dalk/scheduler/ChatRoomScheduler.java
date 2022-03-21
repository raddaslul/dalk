package com.dalk.scheduler;

import com.dalk.domain.ChatRoom;
import com.dalk.repository.ChatRoomRepository;
import com.dalk.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class ChatRoomScheduler {

    private final ChatRoomRepository chatRoomRepository;
    private final BoardService boardService;

    @Scheduled(cron = "0/1 * * * * *")
    public void autoRoomDelete() {
        String now = String.valueOf(LocalDateTime.now());
//        log.info("now = {}", now);
        String nowDate = now.split("T")[0];
//        log.info("nowDate = {}", nowDate);

        Long nowYear = Long.parseLong(nowDate.substring(0,4)) * 31536000;
//        log.info("nowYear = {}", nowYear);

        Long nowMonth = Long.parseLong(nowDate.substring(5,7));
        if (nowMonth == 1 || nowMonth == 3 || nowMonth == 5 || nowMonth == 7 || nowMonth == 8 || nowMonth == 10 || nowMonth == 12)
            nowMonth *= 2678400;
        else if (nowMonth == 4 || nowMonth == 6 || nowMonth == 9 || nowMonth == 11)
            nowMonth *= 2592000;
        else nowMonth *= 2419200;
//        log.info("nowMonth = {}", nowMonth);

        Long nowDay = Long.parseLong(nowDate.substring(8,10)) * 86400;
//        log.info("nowDay = {}", nowDay);

        String nowTime = now.split("T")[1].split("\\.")[0];
//        log.info("nowTime = {}", nowTime);
        Long nowHour = Long.parseLong(nowTime.substring(0,2)) * 3600;
//        log.info("nowHour = {}", nowHour);
        Long nowMinute = Long.parseLong(nowTime.substring(3,5)) * 60;
//        log.info("nowMinute = {}", nowMinute);
        Long nowSecond = Long.parseLong(nowTime.substring(6,8));
//        log.info("nowSecond = {}", nowSecond);
        Long resultNow = nowYear + nowMonth + nowDay + nowHour + nowMinute + nowSecond;
//        log.info("지금 시간 = {}", resultNow);

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        for (ChatRoom chatRoom : chatRoomList) {
//            Long resultCreatedAt = 0L;
//            if(chatRoom.getStatus()) {
                String createdAt = String.valueOf(chatRoom.getCreatedAt());
                // log.info("createdAt = {}", createdAt);
                String createdAtDate = createdAt.split("T")[0];
//            log.info("createdAtDate = {}", createdAtDate);

                Long createdAtYear = Long.parseLong(createdAtDate.substring(0,4)) * 31536000;
//            log.info("createdAtYear = {}", createdAtYear);

                Long createdAtMonth = Long.parseLong(createdAtDate.substring(5,7));
                if (createdAtMonth == 1 || createdAtMonth == 3 || createdAtMonth == 5 || createdAtMonth == 7 || createdAtMonth == 8 || createdAtMonth == 10 || createdAtMonth == 12)
                    createdAtMonth *= 2678400;
                else if (createdAtMonth == 4 || createdAtMonth == 6 || createdAtMonth == 9 || createdAtMonth == 11)
                    createdAtMonth *= 2592000;
                else createdAtMonth *= 2419200;
//            log.info("createdAtMonth = {}", createdAtMonth);

                Long createdAtDay = Long.parseLong(createdAtDate.substring(8,10)) * 86400;
//                log.info("createdAtDay = {}", createdAtDay);

                String createdAtTime = createdAt.split("T")[1].split("\\.")[0];
//            log.info("createdAtTime = {}", createdAtTime);
                Long createdHour = Long.parseLong(createdAtTime.substring(0,2)) * 3600;
//            log.info("createdHour = {}", createdHour);
                Long createdAtMinute = Long.parseLong(createdAtTime.substring(3,5)) * 60;
//            log.info("createdAtMinute = {}", createdAtMinute);
                Long createdAtSecond = Long.parseLong(createdAtTime.substring(6,8));
//            log.info("createdAtSecond = {}", createdAtSecond);
                Long resultCreatedAt = createdAtYear + createdAtMonth + createdAtDay + createdHour + createdAtMinute + createdAtSecond;
//            log.info("생성 시간 = {}", resultCreatedAt);
//            }
            if(chatRoom.getTime()) {
                if (resultNow - resultCreatedAt >= 40) {
                    boardService.createBoard(chatRoom);
                }
            }
            else if (!chatRoom.getTime()){
                if (resultNow - resultCreatedAt >= 3600) {
                    boardService.createBoard(chatRoom);
                }
            }
        }
    }

}
