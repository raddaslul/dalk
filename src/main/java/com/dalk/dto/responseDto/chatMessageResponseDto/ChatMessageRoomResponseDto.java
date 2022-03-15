package com.dalk.dto.responseDto.chatMessageResponseDto;

import com.dalk.domain.ChatMessage;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.Data;

@Data
public class ChatMessageRoomResponseDto {
    private UserInfoResponseDto userInfo;
    private String message;
    private String createdAt;

    public ChatMessageRoomResponseDto(User user, ChatMessage chatMessage) {
        this.userInfo = new UserInfoResponseDto(user);
        this.message = chatMessage.getMessage();
        this.message = chatMessage.getCreatedAt();
    }
}
