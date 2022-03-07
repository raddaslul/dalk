package com.dalk.service;

import com.dalk.domain.ChatRoom;

import com.dalk.dto.requestDto.ChatRoomRequestDto;
import com.dalk.exception.ex.ChatRoomNotFoundException;
import com.dalk.repository.ChatRoomRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    private final ChatRoomRepository chatRoomRepository;
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId 와 채팅룸 id 를 맵핑한 정보 저장

    // 채팅방 생성
    public ChatRoom createChatRoom(ChatRoomRequestDto requestDto, UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        ChatRoom chatRoom = new ChatRoom(requestDto, userId);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    // 전체 채팅방 조회
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAllByOrderByCreatedAtDesc();
    }

//    // 카테고리별 채팅방 조회
//    public List<ChatRoom> getAllChatRoomsByCategory(String category) {
//        return chatRoomRepository.findByCategory(category);
//    }

    // 개별 채팅방 조회
    public ChatRoom getEachChatRoom(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow(
                () -> new ChatRoomNotFoundException("찾는 채팅방이 존재하지 않습니다.")
        );
        return chatRoom;
    }

    // 유저가 입장한 채팅방 ID 와 유저 세션 ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방 ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

}