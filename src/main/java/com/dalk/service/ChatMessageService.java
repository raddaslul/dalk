package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageItemResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageResponseDto;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageAccessResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final ItemRepository itemRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageItemRepository chatMessageItemRepository;

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
        log.info("채팅방 출입 메세지 발송 시 roomID = {}", chatMessageRequestDto.getRoomId());
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(IllegalAccessError::new);
        log.info("service 넘어 왔을 때 user = {}", user);
        if (ChatMessage.MessageType.ENTER.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에 입장했습니다.");

            ChatMessageItem chatMessageItem = chatMessageItemRepository.findByRoomId(chatMessageRequestDto.getRoomId());
            String item = chatMessageItem.getItem();
            if (item.equals("onlyMe")) {
                chatMessageRequestDto.setOnlyMe(user.getNickname());
            }
            else if (item.equals("myName")) {
                chatMessageRequestDto.setMyName(user.getNickname());
            }
            ChatMessageAccessResponseDto chatMessageAccessResponseDto = new ChatMessageAccessResponseDto(chatMessageRequestDto);
//            this.itemChatMessage(chatMessageRequestDto);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageAccessResponseDto);

        } else if (ChatMessage.MessageType.QUIT.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에서 나갔습니다.");
            ChatMessageAccessResponseDto chatMessageAccessResponseDto = new ChatMessageAccessResponseDto(chatMessageRequestDto);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageAccessResponseDto);
        }
    }

    // 채팅방에서 메세지 발송
    public void sendChatMessage(ChatMessage chatMessage, ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findById(chatMessage.getUser().getId())
                .orElseThrow(IllegalAccessError::new);
        log.info("sendChatMessage user= {}", user);

        Point point = pointRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());
        log.info("sendChatMessage point= {}", point);
        List<ItemResponseDto> items = new ArrayList<>();
        for (ItemResponseDto itemResponseDto : items) {
            Item item = itemRepository.findByUser(user);
            String itemName = item.getItemName();
            Integer quantity = item.getQuantity();
            itemResponseDto = new ItemResponseDto(itemName, quantity);
            items.add(itemResponseDto);
        }
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user, point, items);
        Boolean bigFont = chatMessageRequestDto.getBigFont();
        ChatMessageResponseDto chatMessageResponseDto = new ChatMessageResponseDto(chatMessage, bigFont, userInfoResponseDto);
        log.info("ResponseDto message = {}", chatMessageResponseDto.getMessage());
        log.info("ResponseDto bigFont = {}", chatMessageResponseDto.getBigFont());
        log.info("ResponseDto createdAt = {}", chatMessageResponseDto.getCreatedAt());
        log.info("ResponseDto userInfo = {}", chatMessageResponseDto.getUserInfo());
        log.info("ResponseDto roomId = {}", chatMessageResponseDto.getRoomId());
        log.info("ResponseDto type = {}", chatMessageResponseDto.getType());
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
        }
        ChatMessageItem chatMessageItem = new ChatMessageItem(chatMessageRequestDto);
        chatMessageItemRepository.save(chatMessageItem);
        ChatMessageItemResponseDto chatMessageItemResponseDto = new ChatMessageItemResponseDto(chatMessageItem);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageItemResponseDto);
//        this.itemDeleteMessage(chatMessageRequestDto);
    }

    // 아이템 사용시간 지난 후 아이템 삭제
//    public void itemDeleteMessage(ChatMessageRequestDto chatMessageRequestDto) {
//        User user = userRepository.findById(chatMessageRequestDto.getUserId())
//                .orElseThrow(IllegalAccessError::new);
//        String nickname = user.getNickname();
//        String item = redisRepository.getItem(chatMessageRequestDto.getRoomId());
//        redisRepository.removeItem(chatMessageRequestDto.getRoomId());
//        chatMessageRequestDto.setType(ChatMessage.MessageType.DELETE);
//        chatMessageRequestDto.setItem(null);
//        chatMessageRequestDto.setMessage(nickname + "님의" + item + "사용시간이 끝났습니다.");
//        ChatMessageItemResponseDto chatMessageItemResponseDto = new ChatMessageItemResponseDto(chatMessageRequestDto, item);
//        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageItemResponseDto);
//    }

//    public Page<ChatMessage> getChatMessageByRoomId(String roomId, Pageable pageable) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
//        pageable = PageRequest.of(page, 150);
//        return chatMessageRepository.findByRoomId(roomId, pageable);
//    }

}
