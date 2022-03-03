package com.dalk.dto.responseDto;

import com.dalk.domain.ChatMessage;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private ChatMessage.MessageType type;
    private String roomId;
    private String message;
    private String createdAt;
    private UserInfoResponseDto userInfo;

    public ChatMessageResponseDto(
            ChatMessage chatMessage,
            UserInfoResponseDto userInfoResponseDto
    ) {
        this.type = chatMessage.getType();
        this.roomId = chatMessage.getRoomId();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
        this.userInfo = userInfoResponseDto;
    }
}