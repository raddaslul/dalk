package com.dalk.service;

import com.dalk.domain.ChatMessage;
import com.dalk.domain.ChatMessageItem;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageItemResponseDto;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.ChatMessageItemRepository;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class ChatItemService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final ChatMessageItemRepository chatMessageItemRepository;


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

    public void chatItem(ChatMessageRequestDto chatMessageRequestDto, User user) throws IOException, NoSuchAlgorithmException {
        String nickname = user.getNickname();
        if (chatMessageRequestDto.getPapago() != null) {
            if (!nickname.equals(chatMessageRequestDto.getPapago())) {
                String message = ItemService.papago(chatMessageRequestDto.getMessage());
                chatMessageRequestDto.setMessage(message);
            }
        } else if (chatMessageRequestDto.getReverse() != null) {
            if (!nickname.equals(chatMessageRequestDto.getReverse())) {
                String message = ItemService.reverseWord(chatMessageRequestDto.getMessage());
                chatMessageRequestDto.setMessage(message);
            }
        }
    }

    public void enterItem(ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessageItem chatMessageItem = chatMessageItemRepository.findByRoomId(chatMessageRequestDto.getRoomId());
        if (chatMessageItem != null) {
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
    }
}
