package com.dalk.scheduler;

import com.dalk.domain.ChatMessageItem;
import com.dalk.repository.ChatMessageItemRepository;
import com.dalk.service.ChatMessageService;
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
    private final ChatMessageService chatMessageService;

    @Scheduled(cron = "0/1 * * * * *")
    public void autoItemDelete() {
        List<ChatMessageItem> chatMessageItemList = chatMessageItemRepository.findAll();
        String now = String.valueOf(LocalDateTime.now()).split("T")[1];
        log.info("now = {}", now);
        String nowTime = now.split("\\.")[0];
        log.info("nowTime = {}", nowTime);
        Long nowMinute = Long.parseLong(nowTime.substring(4,5)) * 60;
        log.info("nowMinute = {}", nowMinute);
        Long nowSecond = Long.parseLong(nowTime.substring(6,8));
        log.info("nowSecond = {}", nowSecond);
        Long resultNow = nowMinute + nowSecond;
        log.info("지금 시간 = {}", resultNow);

        for (ChatMessageItem chatMessageItem : chatMessageItemList) {
            String roomId = chatMessageItem.getRoomId();
            String item = chatMessageItem.getItem();
            String createdAt = String.valueOf(chatMessageItem.getCreatedAt()).split("T")[1];
            String createdAtTime = createdAt.split("\\.")[0];
            Long createdAtMinute = Long.parseLong(createdAtTime.substring(4,5)) * 60;
            Long createdAtSecond = Long.parseLong(createdAtTime.substring(6,8));
            Long resultCreatedAt = createdAtMinute + createdAtSecond;
            log.info("생성 시간 = {}", resultCreatedAt);
            if(resultNow - resultCreatedAt >= 30) {
                chatMessageItemRepository.delete(chatMessageItem);
                chatMessageService.itemDeleteMessage(roomId, item);
            }
        }
    }

}
