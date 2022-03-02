package com.dalk.pubsub;

import com.dalk.domain.ChatMessage;
import com.dalk.dto.responseDto.ChatMessageResponseDto;
import com.dalk.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public RedisSubscriber(ObjectMapper objectMapper, SimpMessageSendingOperations messagingTemplate, ChatMessageRepository chatMessageRepository) {
        this.objectMapper = objectMapper;
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
    }

    // Redis 에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber 가 해당 메시지를 받아 처리한다.
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessageResponseDto chatMessageResponseDto = objectMapper.readValue(publishMessage, ChatMessageResponseDto.class);
            System.out.println("구독자 chatMessage : " + chatMessageResponseDto);
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/api/chat/rooms/" + chatMessageResponseDto.getRoomId(), chatMessageResponseDto);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
