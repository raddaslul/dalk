package com.dalk.handler;

import com.dalk.domain.ChatMessage;
import com.dalk.repository.RedisRepository;
import com.dalk.security.jwt.JwtDecoder;
import com.dalk.security.provider.JWTAuthProvider;
import com.dalk.service.ChatMessageService;
import com.dalk.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

//    private final JwtTokenProvider jwtTokenProvider;
    private final ChatMessageService chatService;
    private final JwtDecoder jwtDecoder;
    private final RedisRepository redisRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization").substring(7));
        }

        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            Long roomId = chatService.getRoomId(
                    Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId")
            );
            if (roomId != null) {
                String sessionId = (String) message.getHeaders().get("simpSessionId");
                String name = jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization").substring(7));
                redisRepository.setSessionUserInfo(sessionId, roomId, name);
                redisRepository.setUserChatRoomInOut(roomId + "_" + name, true);
                System.out.println("SUBSCRIBE 클라이언트 헤더" + message.getHeaders());
                System.out.println("SUBSCRIBE 클라이언트 세션 아이디" + sessionId);
                System.out.println("SUBSCRIBE 클라이언트 유저 이름: " + name);
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
//        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
//            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
//            // roomId를 URL로 전송해주고 있어 추출 필요
//            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
//
//
//            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
//            // sessionId는 정상적으로 들어가고있음
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            chatRoomService.setUserEnterInfo(sessionId, roomId);
//
//            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
////            String token = Optional.ofNullable(accessor.getFirstNativeHeader("token")).orElse("UnknownUser");
////            String name = jwtTokenProvider.getAuthenticationUsername(token);  토큰에서 유저네임 뽑아오는 부분
//            String name = jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization").substring(7));
//            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
//
//            log.info("SUBSCRIBED {}, {}", name, roomId);
//        }
//
//        else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
//
//            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            String roomId = chatRoomService.getUserEnterRoomId(sessionId);
//
//            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
////            String token = Optional.ofNullable(accessor.getFirstNativeHeader("token")).orElse("UnknownUser");
//
//            if(accessor.getFirstNativeHeader("token") != null) {
////                String name = jwtTokenProvider.getAuthenticationUsername(token);  토큰에서 유저네임 뽑아오는 부분
//                String name = jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization").substring(7));
//                chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).build());
//            }
//
//            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
//            chatRoomService.removeUserEnterInfo(sessionId);
//            log.info("DISCONNECT {}, {}", sessionId, roomId);
//        }
//        return message;
    }

}
