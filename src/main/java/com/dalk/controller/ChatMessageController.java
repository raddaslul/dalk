package com.dalk.controller;


import com.dalk.domain.ChatMessage;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.security.jwt.JwtDecoder;
import com.dalk.service.ChatMessageService;
import com.dalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@CrossOrigin(origins = "*")
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final UserService userService;
//    private final JwtTokenProvider jwtTokenProvider;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService, UserService userService, JwtDecoder jwtDecoder) {
        this.chatMessageService = chatMessageService;
        this.userService = userService;
//        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtDecoder = jwtDecoder;
    }

    // 채팅 메시지를 @MessageMapping 형태로 받는다
    // 웹소켓으로 publish 된 메시지를 받는 곳이다
    @MessageMapping("/api/chat/message")
    public void message(@RequestBody ChatMessageRequestDto messageRequestDto, @Header("token") String token) {

        // 로그인 회원 정보를 들어온 메시지에 값 세팅
//        User user = jwtTokenProvider.getAuthenticationUser(token);
        User user = jwtDecoder.getAuthenticationUser(token);
        messageRequestDto.setUserId(user.getId());
        messageRequestDto.setSender(user.getUsername());

        // 메시지 생성 시간 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        messageRequestDto.setCreatedAt(dateResult);

        // DTO 로 채팅 메시지 객체 생성
        ChatMessage chatMessage = new ChatMessage(messageRequestDto, userService);

        // 웹소켓 통신으로 채팅방 토픽 구독자들에게 메시지 보내기
        chatMessageService.sendChatMessage(chatMessage);

        // MySql DB에 채팅 메시지 저장
        chatMessageService.save(chatMessage);
    }

}
