package com.dalk.service;

import com.dalk.domain.ChatMessage;
import com.dalk.domain.Item;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.dto.responseDto.ChatMessageResponseDto;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.ChatMessageRepository;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
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

    // destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        System.out.println("룸 아이디 destination" + destination);

        if (lastIndex != -1) {
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

    // 채팅방에 메시지 발송
    public void enterChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(IllegalAccessError::new);
        log.info("service 넘어 왔을 때 user = {}", user);
        if (ChatMessage.MessageType.ENTER.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에 입장했습니다.");
            System.out.println("타입 Enter 일 때 ID : " + chatMessageRequestDto.getUserId());
            System.out.println("타입 Enter 일 때 roomId : " + chatMessageRequestDto.getRoomId());
            System.out.println("타입 Enter 일 때 type : " + chatMessageRequestDto.getType());
            System.out.println("타입 Enter 일 때 message : " + chatMessageRequestDto.getMessage());
            System.out.println("타입 Enter 일 때 createdAt : " + chatMessageRequestDto.getCreatedAt());
//            chatMessageRequestDto.set("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessageRequestDto.getType())) {
            chatMessageRequestDto.setMessage(user.getNickname() + "님이 방에서 나갔습니다.");
//            chatMessage.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageRequestDto);
    }

    public void sendChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User user = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(IllegalAccessError::new);

        Point point = pointRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());
        List<ItemResponseDto> items = new ArrayList<>();
        for (ItemResponseDto itemResponseDto : items) {
            Item item = itemRepository.findByUser(user);
            String itemName = item.getItemName();
            Integer quantity = item.getQuantity();
            itemResponseDto = new ItemResponseDto(itemName, quantity);
            items.add(itemResponseDto);
        }
        ChatMessage chatMessage = chatMessageRepository.findByRoomId(chatMessageRequestDto.getRoomId());
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

//    public Page<ChatMessage> getChatMessageByRoomId(String roomId, Pageable pageable) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
//        pageable = PageRequest.of(page, 150);
//        return chatMessageRepository.findByRoomId(roomId, pageable);
//    }

}
