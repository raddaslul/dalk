package com.dalk.config.socket;

import com.dalk.domain.ChatMessage;
import com.dalk.domain.ChatRoom;
import com.dalk.domain.ChatRoomUser;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.exception.ex.ChatRoomNotFoundException;
import com.dalk.exception.ex.DuplicateChatRoomUserException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.exception.ex.UserNotFoundException;
import com.dalk.repository.ChatRoomRepository;
import com.dalk.repository.ChatRoomUserRepository;
import com.dalk.repository.RedisRepository;
import com.dalk.repository.UserRepository;
import com.dalk.security.jwt.JwtDecoder;
import com.dalk.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Transactional
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtDecoder jwtDecoder;
    private final ChatMessageService chatMessageService;
    private final RedisRepository redisRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    // websocket 을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = (String) message.getHeaders().get("simpSessionId");

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("Authorization").substring(7);
            if (jwtDecoder.decodeUserId(token) == null) {
                throw new LoginUserNotFoundException("로그인을 해주시기 바랍니다.");
            }

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("Authorization").substring(7);
            Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

            String roomId = getRoomId(
                    Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));

            ChatRoom chatRoom = chatRoomRepository.findById(Long.valueOf(roomId))
                    .orElseThrow(() -> new ChatRoomNotFoundException("해당 토론방이 존재하지 않습니다."));

            saveChatRoomUser(user, chatRoom);

            redisRepository.setSessionRoomId(sessionId, roomId);

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String rawToken = Optional.ofNullable(accessor.getFirstNativeHeader("Authorization")).orElse("unknownUser");
            if (!rawToken.equals("unknownUser")) {
                String token = rawToken.substring(7);
                Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
                chatRoomUserRepository.deleteByUser_Id(userId);
                String roomId = redisRepository.getSessionRoomId(sessionId);
                chatMessageService.accessChatMessage(ChatMessageRequestDto.builder().type(ChatMessage.MessageType.EXIT).roomId(roomId).userId(userId).build());
                redisRepository.removeSessionRoomId(sessionId);
            }
        }
        return message;
    }

    // destination 정보에서 roomId 추출
    private String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return null;
        }
    }

    private void saveChatRoomUser(User user, ChatRoom chatRoom) {
        ChatRoomUser chatRoomOldUser = chatRoomUserRepository.findByUser_Id(user.getId());
        if (chatRoomOldUser == null) {
            ChatRoomUser chatRoomUser = new ChatRoomUser(chatRoom, user);
            chatRoomUserRepository.save(chatRoomUser);
        } else throw new DuplicateChatRoomUserException("이미 다른 토론방에 있습니다.");
    }
}