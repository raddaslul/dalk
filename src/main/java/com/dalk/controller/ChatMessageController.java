package com.dalk.controller;

import com.dalk.domain.ChatMessage;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.repository.UserRepository;
import com.dalk.security.jwt.JwtDecoder;
import com.dalk.service.ChatMessageService;
import com.dalk.service.ItemService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    // 채팅 메시지를 @MessageMapping 형태로 받는다
    // 웹소켓으로 publish 된 메시지를 받는 곳이다
    @MessageMapping("/chat/message")
    @ApiOperation(value = "채팅 메세지 수신")
    public void message(@RequestBody ChatMessageRequestDto chatMessageRequestDto, @Header("Authorization") String rawToken) throws IOException, NoSuchAlgorithmException {
        String token = rawToken.substring(7);
        // 로그인 회원 정보를 들어온 메시지에 값 세팅
        Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
        chatMessageRequestDto.setUserId(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));
        String nickname = user.getNickname();

// MySql DB에 채팅 메시지 저장 
      if (chatMessageRequestDto.getType().equals(ChatMessage.MessageType.TALK)) { 
        if(chatMessageRequestDto.getPapago() != null) { 
          if(!nickname.equals(chatMessageRequestDto.getPapago())) { 
            String message = ItemService.papago(chatMessageRequestDto.getMessage()); 
            chatMessageRequestDto.setMessage(message); 
          } 
        }   else if(chatMessageRequestDto.getReverse() != null) { 
          if(!nickname.equals(chatMessageRequestDto.getReverse())){ 
            String message = ItemService.reverseWord(chatMessageRequestDto.getMessage()); 
            chatMessageRequestDto.setMessage(message); 
          } 
        } 
        ChatMessage chatMessage = chatMessageService.save(chatMessageRequestDto); 
        chatMessageService.sendChatMessage(chatMessage, chatMessageRequestDto); 
      }  else if (chatMessageRequestDto.getType().equals(ChatMessage.MessageType.ITEM)) { 
        chatMessageService.itemChatMessage(chatMessageRequestDto); 
      } 
    } 
}

