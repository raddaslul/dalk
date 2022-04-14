package com.dalk.controller;

import com.dalk.domain.ChatMessage;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.security.jwt.JwtDecoder;
import com.dalk.service.ChatItemService;
import com.dalk.service.ChatMessageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatItemService chatItemService;
    private final JwtDecoder jwtDecoder;

    @MessageMapping("/chat/enter")
    @ApiOperation(value = "채팅방 입장")
    public void enterMessage(@RequestBody ChatMessageRequestDto chatMessageRequestDto,
                             @Header("Authorization") String rawToken) {
        String token = rawToken.substring(7);
        Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
        chatMessageRequestDto.setUserId(userId);
        chatMessageService.accessChatMessage(chatMessageRequestDto);
    }

    @MessageMapping("/chat/message")
    @ApiOperation(value = "채팅 메세지 수신")
    public void message(@RequestBody ChatMessageRequestDto chatMessageRequestDto,
                        @Header("Authorization") String rawToken) throws IOException, NoSuchAlgorithmException {
        String token = rawToken.substring(7);
        Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
        chatMessageRequestDto.setUserId(userId);

        if (chatMessageRequestDto.getType().equals(ChatMessage.MessageType.TALK)) {
            ChatMessage chatMessage = chatMessageService.save(chatMessageRequestDto);
            chatMessageService.sendChatMessage(chatMessage, chatMessageRequestDto);
        } else if (chatMessageRequestDto.getType().equals(ChatMessage.MessageType.ITEM)) {
            chatItemService.itemChatMessage(chatMessageRequestDto);
        }
    }
}
