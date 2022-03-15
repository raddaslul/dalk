package com.dalk.config.socket;


import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageExitResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageItemResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageEnterResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    // Redis 에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber 가 해당 메시지를 받아 처리한다.
    public void sendMessage(String publishMessage) {
        log.info("RedisSubscriber sendMessage publishMessage = {}", publishMessage);
        log.info("publishMessage 새롭게 자른거 = {}", publishMessage.startsWith("ENTER", 9));
        log.info("publishMessage 새롭게 자른거 = {}", publishMessage.startsWith("TALK", 9));
        log.info("publishMessage 새롭게 자른거 = {}", publishMessage.startsWith("ITEM", 9));
        try {
            // 채팅방 입장시 메세지 보내기
            if (publishMessage.startsWith("ENTER", 9)) {
                ChatMessageEnterResponseDto chatMessageEnterResponseDto = objectMapper.readValue(publishMessage, ChatMessageEnterResponseDto.class);
                messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatMessageEnterResponseDto.getRoomId(), chatMessageEnterResponseDto);
            }

            else if (publishMessage.startsWith("EXIT", 9)) {
                ChatMessageExitResponseDto chatMessageExitResponseDto = objectMapper.readValue(publishMessage, ChatMessageExitResponseDto.class);
                messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatMessageExitResponseDto.getRoomId(), chatMessageExitResponseDto);
            }

            // 채팅방에서 채팅 시 메세지 보내기
            else if(publishMessage.startsWith("TALK", 9)){
                ChatMessageResponseDto chatMessageResponseDto = objectMapper.readValue(publishMessage, ChatMessageResponseDto.class);
                messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatMessageResponseDto.getRoomId(), chatMessageResponseDto);
            }

            // 채팅방에서 아이템 사용시 메세지 보내기
            else if(publishMessage.startsWith("ITEM", 9)){
                ChatMessageItemResponseDto chatMessageItemResponseDto = objectMapper.readValue(publishMessage, ChatMessageItemResponseDto.class);
                messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatMessageItemResponseDto.getRoomId(), chatMessageItemResponseDto);
            }

            else if(publishMessage.startsWith("ITEMTIMEOUT", 9)){
                ChatMessageItemResponseDto chatMessageItemResponseDto = objectMapper.readValue(publishMessage, ChatMessageItemResponseDto.class);
                messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatMessageItemResponseDto.getRoomId(), chatMessageItemResponseDto);
            }

        } catch (Exception e) {
            log.error("Exception = {}", e);
        }
    }
}
