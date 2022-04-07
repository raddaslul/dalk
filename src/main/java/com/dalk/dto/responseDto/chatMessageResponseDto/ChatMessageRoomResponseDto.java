package com.dalk.dto.responseDto.chatMessageResponseDto;

import com.dalk.domain.ChatMessage;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.CreatorInfoResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.Data;

@Data
public class ChatMessageRoomResponseDto {
    private ChatMessage.MessageType type;
    private CreatorInfoResponseDto userInfo;
    private String message;
    private String createdAt;

    public ChatMessageRoomResponseDto(ChatMessage chatMessage) {
        this.type = chatMessage.getType();
        if (chatMessage.getUser() == null) {
            this.userInfo = new CreatorInfoResponseDto(null);
        }
        this.userInfo = new CreatorInfoResponseDto(chatMessage.getUser());
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
