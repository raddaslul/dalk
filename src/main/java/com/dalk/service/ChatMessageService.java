package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageExitResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageItemResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageEnterResponseDto;
import com.dalk.exception.ex.ChatRoomNotFoundException;
import com.dalk.exception.ex.DuplicateChatRoomUserException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageItemRepository chatMessageItemRepository;
    private final ChatRoomRepository chatRoomRepository;

    // destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        log.info("destination = {}", destination);
        if (lastIndex != -1) {
            log.info("destination roomId = {}", destination.substring(lastIndex +1));
            return destination.substring(lastIndex + 1);
        } else {
            return null;
        }
    }

    @Transactional
    public ChatMessage save(ChatMessageRequestDto chatMessageRequestDto) {
        // 메시지 생성 시간 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        chatMessageRequestDto.setCreatedAt(dateResult);

        // 유저 정보 삽입
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(IllegalAccessError::new);

        ChatMessage chatMessage = new ChatMessage(chatMessageRequestDto, user);
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

            if (chatMessageItemRepository.findByRoomId(chatMessageRequestDto.getRoomId()) != null) {
                ChatMessageItem chatMessageItem = chatMessageItemRepository.findByRoomId(chatMessageRequestDto.getRoomId());
                User itemUser = userRepository.findById(chatMessageItem.getUserId())
                        .orElseThrow(() -> new LoginUserNotFoundException("로그인 후 이용해 주시기 바랍니다."));
                String item = chatMessageItem.getItem();
                switch (item) {
                    case "onlyMe":
                        chatMessageRequestDto.setOnlyMe(itemUser.getNickname());
                        break;
                    case "myName":
                        chatMessageRequestDto.setMyName(itemUser.getNickname());
                        break;
                    case "papago":
                        chatMessageRequestDto.setPapago(itemUser.getNickname());
                        break;
                    case "reverse":
                        chatMessageRequestDto.setReverse(itemUser.getNickname());
                        break;
                }
            }
            ChatMessageEnterResponseDto chatMessageEnterResponseDto = new ChatMessageEnterResponseDto(chatMessageRequestDto, user);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageEnterResponseDto);
        } else if (ChatMessage.MessageType.EXIT.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에서 나갔습니다.");
//            chatRoomUserRepository.deleteByUser_Id(user.getId());
            if(chatRoom.getChatRoomUser() != null) {
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

    // 채팅방에서 아이템 사용
    @Transactional
    public void itemChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(IllegalAccessError::new);
        String nickname = user.getNickname();
        String item = chatMessageRequestDto.getItem();

        switch (item) {
            case "onlyMe":
                chatMessageRequestDto.setOnlyMe(nickname);
                chatMessageRequestDto.setMessage(nickname + "님이 나만 말하기를 사용하셨습니다.");
                break;
            case "myName":
                chatMessageRequestDto.setMyName(nickname);
                chatMessageRequestDto.setMessage(nickname + "님이 내이름으로를 사용하셨습니다.");
                break;
            case "papago":
                chatMessageRequestDto.setPapago(nickname);
                chatMessageRequestDto.setMessage(nickname + "님이 파파고를 사용하셨습니다.");
                break;
            case "reverse":
                chatMessageRequestDto.setReverse(nickname);
                chatMessageRequestDto.setMessage(nickname + "님이 로꾸꺼를 사용하셨습니다.");
                break;
        }
        ChatMessageItem chatMessageItem = new ChatMessageItem(chatMessageRequestDto);
        chatMessageItemRepository.save(chatMessageItem);
        ChatMessageItemResponseDto chatMessageItemResponseDto = new ChatMessageItemResponseDto(chatMessageItem);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageItemResponseDto);
    }

    // 아이템 사용시간 지난 후 아이템 삭제
    @Transactional
    public void itemDeleteMessage(String roomId, String item) {
        ChatMessageItemResponseDto chatMessageItemResponseDto = new ChatMessageItemResponseDto();
        chatMessageItemResponseDto.setRoomId(roomId);
        chatMessageItemResponseDto.setItem(null);
        chatMessageItemResponseDto.setType(ChatMessage.MessageType.ITEMTIMEOUT);
        switch (item) {
            case "onlyMe":
                chatMessageItemResponseDto.setMessage("나만 말하기 사용시간이 완료되었습니다.");
                break;
            case "myName":
                chatMessageItemResponseDto.setMessage("내 이름으로 사용시간이 완료되었습니다.");
                break;
            case "papago":
                chatMessageItemResponseDto.setMessage("파파고 사용시간이 완료되었습니다.");
                break;
            case "reverse":
                chatMessageItemResponseDto.setMessage("로꾸꺼 사용시간이 완료되었습니다.");
                break;
        }
        chatMessageItemResponseDto.setOnlyMe(null);
        chatMessageItemResponseDto.setMyName(null);
        chatMessageItemResponseDto.setPapago(null);
        chatMessageItemResponseDto.setReverse(null);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageItemResponseDto);
    }
}