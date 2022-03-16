package com.dalk.config.socket;

import com.dalk.domain.ChatMessage;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.RedisRepository;
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

    // websocket 을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = (String) message.getHeaders().get("simpSessionId");

        // websocket 연결시 헤더의 jwt token 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("Authorization").substring(7);
            Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
            if(userId == null) {
                throw new LoginUserNotFoundException("로그인을 해주시기 바랍니다.");
            }
        }

        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("Authorization").substring(7);
//            String username = jwtDecoder.decodeUsername(token);
            Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
            log.info("SUBSCRIBE 할 때 token = {}", token );
            String roomId = chatMessageService.getRoomId(
                    Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId")
            );
            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            if(userId != null) {
                redisRepository.setSessionRoomId(sessionId, roomId);

                // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
                chatMessageService.accessChatMessage(ChatMessageRequestDto.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).userId(userId).build());
            } else throw new LoginUserNotFoundException("로그인을 해주시기 바랍니다.");
        }

        else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("Authorization").substring(7);
            Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
            String roomId = redisRepository.getSessionRoomId(sessionId);
            chatMessageService.accessChatMessage(ChatMessageRequestDto.builder().type(ChatMessage.MessageType.EXIT).roomId(roomId).userId(userId).build());
            redisRepository.removeUserEnterInfo(sessionId);
        }
        return message;
    }
}