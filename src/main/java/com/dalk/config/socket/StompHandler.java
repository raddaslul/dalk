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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

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
        // websocket 연결시 헤더의 jwt token 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String username = jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization").substring(7));
            log.info("CONNECT 할 때 username = {}", username);
            if(username == null) {
                log.info("CONNECT 할 때");
                throw new LoginUserNotFoundException("로그인을 해주시기 바랍니다.");
            }
        }

        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String roomId = chatMessageService.getRoomId(
                    Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId")
            );
            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
            if (roomId != null) {
                String token = accessor.getFirstNativeHeader("Authorization").substring(7);
                log.info("SUBSCRIBE 할 때 token = {}", token );
                String username = jwtDecoder.decodeUsername(token);
                Long userId = Long.parseLong(jwtDecoder.decodeUserId(token));
                String sessionId = (String) message.getHeaders().get("simpSessionId");
                if(username != null) {
                    redisRepository.setSessionUserInfo(sessionId, roomId, username);
                    redisRepository.setUserChatRoomInOut(roomId + "_" + username, true);
                    log.info("SUBSCRIBE 할 때 roomId = {}", roomId);
                    log.info("SUBSCRIBE 할 때 sessionId = {}", sessionId);
                    log.info("SUBSCRIBE 할 때 username = {}", username);

                    // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                    String dateResult = sdf.format(date);
                    chatMessageService.enterChatMessage(
                        ChatMessageRequestDto.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).userId(userId).createdAt(dateResult).bigFont(false).build());
                    log.info("SUBSCRIBE 할 때");
                } throw new LoginUserNotFoundException("로그인을 해주시기 바랍니다.");
            }

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String findInOutKey = redisRepository.getSessionUserInfo(sessionId);
            System.out.println("DISCONNECT 클라이언트 sessionId: " + sessionId);
            System.out.println("DISCONNECT 클라이언트 inoutKey: " + findInOutKey);

            if (findInOutKey != null) {
                redisRepository.setUserChatRoomInOut(findInOutKey, false);
            }

            redisRepository.removeUserEnterInfo(sessionId);
        }
        return message;
    }
}