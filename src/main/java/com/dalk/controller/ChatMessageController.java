package com.dalk.controller;

import com.dalk.domain.ChatMessage;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.security.jwt.JwtDecoder;
import com.dalk.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final JwtDecoder jwtDecoder;

    // 채팅 메시지를 @MessageMapping 형태로 받는다
    // 웹소켓으로 publish 된 메시지를 받는 곳이다
    @MessageMapping("/chat/message")
    public void message(@RequestBody ChatMessageRequestDto chatMessageRequestDto, @Header("Authorization") String token) {
        token = token.substring(7);
        // 로그인 회원 정보를 들어온 메시지에 값 세팅
        chatMessageRequestDto.setUserId(Long.parseLong(jwtDecoder.decodeUID(token)));

        // MySql DB에 채팅 메시지 저장
        ChatMessage chatMessage = chatMessageService.save(chatMessageRequestDto);

        // 웹소켓 통신으로 채팅방 토픽 구독자들에게 메시지 보내기
        chatMessageService.sendChatMessage(chatMessage);

    }

}
