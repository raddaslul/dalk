package com.dalk.dto.responseDto.chatMessageResponseDto;

import com.dalk.domain.ChatMessage;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageExitResponseDto {
    private ChatMessage.MessageType type;
    private String roomId;
    private String message;
    private UserInfoResponseDto userInfo;

    public ChatMessageExitResponseDto(ChatMessageRequestDto chatMessageRequestDto, User user) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.message = chatMessageRequestDto.getMessage();
        this.userInfo = new UserInfoResponseDto(user);
    }
}
