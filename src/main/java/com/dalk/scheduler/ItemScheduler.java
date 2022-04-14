package com.dalk.scheduler;

import com.dalk.domain.ChatMessageItem;
import com.dalk.repository.ChatMessageItemRepository;
import com.dalk.service.ChatItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemScheduler {

    private final ChatMessageItemRepository chatMessageItemRepository;
    private final ChatItemService chatItemService;

    @Scheduled(cron = "0/1 * * * * *")
    public void autoItemDelete() {
        List<ChatMessageItem> chatMessageItemList = chatMessageItemRepository.findAll();
        String now = String.valueOf(LocalDateTime.now());
        String nowDate = now.split("T")[0];

        Long nowYear = Long.parseLong(nowDate.substring(0,4)) * 31536000;

        Long nowMonth = Long.parseLong(nowDate.substring(5,7));
        if (nowMonth == 1 || nowMonth == 3 || nowMonth == 5 || nowMonth == 7 || nowMonth == 8 || nowMonth == 10 || nowMonth == 12)
            nowMonth *= 2678400;
        else if (nowMonth == 4 || nowMonth == 6 || nowMonth == 9 || nowMonth == 11)
            nowMonth *= 2592000;
        else nowMonth *= 2419200;

        Long nowDay = Long.parseLong(nowDate.substring(8,10)) * 86400;

        String nowTime = now.split("T")[1].split("\\.")[0];
        Long nowHour = Long.parseLong(nowTime.substring(0,2)) * 3600;
        Long nowMinute = Long.parseLong(nowTime.substring(3,5)) * 60;
        Long nowSecond = Long.parseLong(nowTime.substring(6,8));
        Long resultNow = nowYear + nowMonth + nowDay + nowHour + nowMinute + nowSecond;

        for (ChatMessageItem chatMessageItem : chatMessageItemList) {
            String roomId = chatMessageItem.getRoomId();
            String item = chatMessageItem.getItem();

            String createdAt = String.valueOf(chatMessageItem.getCreatedAt());
            String createdAtDate = createdAt.split("T")[0];

            Long createdAtYear = Long.parseLong(createdAtDate.substring(0,4)) * 31536000;

            Long createdAtMonth = Long.parseLong(createdAtDate.substring(5,7));
            if (createdAtMonth == 1 || createdAtMonth == 3 || createdAtMonth == 5 || createdAtMonth == 7 || createdAtMonth == 8 || createdAtMonth == 10 || createdAtMonth == 12)
                createdAtMonth *= 2678400;
            else if (createdAtMonth == 4 || createdAtMonth == 6 || createdAtMonth == 9 || createdAtMonth == 11)
                createdAtMonth *= 2592000;
            else createdAtMonth *= 2419200;

            Long createdAtDay = Long.parseLong(createdAtDate.substring(8,10)) * 86400;

            String createdAtTime = createdAt.split("T")[1].split("\\.")[0];
            Long createdHour = Long.parseLong(createdAtTime.substring(0,2)) * 3600;
            Long createdAtMinute = Long.parseLong(createdAtTime.substring(3,5)) * 60;
            Long createdAtSecond = Long.parseLong(createdAtTime.substring(6,8));
            Long resultCreatedAt = createdAtYear + createdAtMonth + createdAtDay + createdHour + createdAtMinute + createdAtSecond;
            if(resultNow - resultCreatedAt >= 20) {
                chatMessageItemRepository.delete(chatMessageItem);
                chatItemService.itemDeleteMessage(roomId, item);
            }
        }
    }
}
