package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageExitResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageItemResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageEnterResponseDto;
import com.dalk.exception.ex.ChatRoomNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Transactional
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

    public ChatMessage save(ChatMessageRequestDto chatMessageRequestDto) {
        // 메시지 생성 시간 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
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
    public void accessChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(() -> new LoginUserNotFoundException("로그인 후 이용해 주시기 바랍니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(Long.valueOf(chatMessageRequestDto.getRoomId()))
                .orElseThrow(() -> new ChatRoomNotFoundException("채팅방이 존재하지 않습니다."));

        if (ChatMessage.MessageType.ENTER.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에 입장했습니다.");
            ChatRoomUser chatRoomOldUser = chatRoomUserRepository.findAllByChatRoom_IdAndUser_Id(chatRoom.getId(), user.getId());
            if (chatRoomOldUser == null) {
                ChatRoomUser chatRoomUser = new ChatRoomUser(chatRoom, user);
                chatRoomUserRepository.save(chatRoomUser);
            }
            chatRoom.setUserCnt(chatRoom.getChatRoomUser().size());
            chatRoomRepository.save(chatRoom);

            if(chatMessageItemRepository.findByRoomId(chatMessageRequestDto.getRoomId()) != null) {
                ChatMessageItem chatMessageItem = chatMessageItemRepository.findByRoomId(chatMessageRequestDto.getRoomId());
                User itemUser = userRepository.findById(chatMessageItem.getUserId())
                        .orElseThrow(() -> new LoginUserNotFoundException("로그인 후 이용해 주시기 바랍니다."));
                String item = chatMessageItem.getItem();
                if (item.equals("onlyMe")) {
                    chatMessageRequestDto.setOnlyMe(itemUser.getNickname());
                }
                else if (item.equals("myName")) {
                    chatMessageRequestDto.setMyName(itemUser.getNickname());
                }
                else if (item.equals("papago")) {
                    chatMessageRequestDto.setPapago(itemUser.getNickname());
                }
                else if (item.equals("reverse")) {
                    chatMessageRequestDto.setReverse(itemUser.getNickname());
                }
            }
            ChatMessageEnterResponseDto chatMessageEnterResponseDto = new ChatMessageEnterResponseDto(chatMessageRequestDto, user);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageEnterResponseDto);
        } else if (ChatMessage.MessageType.EXIT.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에서 나갔습니다.");
            chatRoomUserRepository.deleteByUser_Id(user.getId());
            chatRoom.setUserCnt(chatRoom.getChatRoomUser().size());
            chatRoomRepository.save(chatRoom);
            ChatMessageExitResponseDto chatMessageExitResponseDto = new ChatMessageExitResponseDto(chatMessageRequestDto, user);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageExitResponseDto);
        }
    }

    // 채팅방에서 메세지 발송
    public void sendChatMessage(ChatMessage chatMessage, ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findById(chatMessage.getUser().getId())
                .orElseThrow(IllegalAccessError::new);
        log.info("sendChatMessage user= {}", user);
        Boolean bigFont = chatMessageRequestDto.getBigFont();
        ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(chatMessage, bigFont);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageResponseDto);
    }

    // 채팅방에서 아이템 사용
    public void itemChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(IllegalAccessError::new);
        String nickname = user.getNickname();
        String item = chatMessageRequestDto.getItem();
        if (item.equals("onlyMe")) {
            chatMessageRequestDto.setOnlyMe(nickname);
            chatMessageRequestDto.setMessage(nickname + "님이" + item + "를 사용하셨습니다.");
        } else if (item.equals("myName")) {
            chatMessageRequestDto.setMyName(nickname);
            chatMessageRequestDto.setMessage(nickname + "님이" +  item + "을 사용하셨습니다.");
        } else if (item.equals("papago")) {
            chatMessageRequestDto.setPapago(nickname);
            chatMessageRequestDto.setMessage(nickname + "님이" +  item + "을 사용하셨습니다.");
        } else if (item.equals("reverse")) {
            chatMessageRequestDto.setReverse(nickname);
            chatMessageRequestDto.setMessage(nickname + "님이" + item + "을 사용하셨습니다.");
        }
        ChatMessageItem chatMessageItem = new ChatMessageItem(chatMessageRequestDto);
        chatMessageItemRepository.save(chatMessageItem);
        ChatMessageItemResponseDto chatMessageItemResponseDto = new ChatMessageItemResponseDto(chatMessageItem);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageItemResponseDto);
    }

//     아이템 사용시간 지난 후 아이템 삭제
    public void itemDeleteMessage(String roomId, String item) {
        ChatMessageItemResponseDto chatMessageItemResponseDto = new ChatMessageItemResponseDto();
        chatMessageItemResponseDto.setRoomId(roomId);
        chatMessageItemResponseDto.setItem(null);
        chatMessageItemResponseDto.setType(ChatMessage.MessageType.ITEMTIMEOUT);
        chatMessageItemResponseDto.setMessage(item + "사용시간이 완료되었습니다.");
        chatMessageItemResponseDto.setOnlyMe(null);
        chatMessageItemResponseDto.setMyName(null);
        chatMessageItemResponseDto.setPapago(null);
        chatMessageItemResponseDto.setReverse(null);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageItemResponseDto);
    }
}
