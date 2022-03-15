package com.dalk.dto.responseDto.chatMessageResponseDto;

import com.dalk.domain.ChatMessage;

import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.service.ItemService;
import com.dalk.service.UserService;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private ChatMessage.MessageType type;
    private String roomId;
    private String message;
    private Boolean bigFont;
    private String createdAt;
    private UserInfoResponseDto userInfo;

    public ChatMessageResponseDto(
            ChatMessage chatMessage,
            Boolean bigFont
    ) {
        this.type = chatMessage.getType();
        this.roomId = chatMessage.getRoomId();
        this.message = chatMessage.getMessage();
        this.bigFont = bigFont;
        this.createdAt = chatMessage.getCreatedAt();
        this.userInfo = UserService.userInfo(chatMessage.getUser());
    }
}
