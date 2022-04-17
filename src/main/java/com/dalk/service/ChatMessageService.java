package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageExitResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageEnterResponseDto;
import com.dalk.exception.ex.ChatRoomNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatItemService chatItemService;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatMessage save(ChatMessageRequestDto chatMessageRequestDto) throws IOException, NoSuchAlgorithmException {

        configDate(chatMessageRequestDto);
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));
        chatItemService.chatItem(chatMessageRequestDto, user);

        ChatRoom chatRoom = chatRoomRepository.findById(Long.valueOf(chatMessageRequestDto.getRoomId()))
                .orElseThrow(() -> new ChatRoomNotFoundException("채팅방이 존재하지 않습니다."));

        ChatMessage chatMessage = new ChatMessage(chatMessageRequestDto, user, chatRoom);
        return chatMessageRepository.save(chatMessage);
    }

    // 채팅방 입출입 시 메시지 발송
    @Transactional
    public void accessChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(() -> new LoginUserNotFoundException("로그인 후 이용해 주시기 바랍니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(Long.valueOf(chatMessageRequestDto.getRoomId()))
                .orElseThrow(() -> new ChatRoomNotFoundException("채팅방이 존재하지 않습니다."));

        if (ChatMessage.MessageType.ENTER.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에 입장했습니다.");
            chatRoom.setUserCnt(chatRoom.getChatRoomUser().size());
            chatRoomRepository.save(chatRoom);
            chatItemService.enterItem(chatMessageRequestDto);
            ChatMessageEnterResponseDto chatMessageEnterResponseDto = new ChatMessageEnterResponseDto(chatMessageRequestDto, user);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageEnterResponseDto);
        } else if (ChatMessage.MessageType.EXIT.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에서 나갔습니다.");

            if (chatRoom.getChatRoomUser() != null) {
                chatRoom.setUserCnt(chatRoom.getChatRoomUser().size());
            } else {
                chatRoom.setUserCnt(0);
            }

            chatRoomRepository.save(chatRoom);
            ChatMessageExitResponseDto chatMessageExitResponseDto = new ChatMessageExitResponseDto(chatMessageRequestDto, user);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageExitResponseDto);
        }
    }

    // 채팅방에서 메세지 발송
    @Transactional
    public void sendChatMessage(ChatMessage chatMessage, ChatMessageRequestDto chatMessageRequestDto) {
        Boolean bigFont = chatMessageRequestDto.getBigFont();
        ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(chatMessage, bigFont);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageResponseDto);
    }

    private void configDate(ChatMessageRequestDto chatMessageRequestDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        chatMessageRequestDto.setCreatedAt(dateResult);
    }
}