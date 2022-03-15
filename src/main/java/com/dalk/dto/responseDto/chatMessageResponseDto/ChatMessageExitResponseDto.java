package com.dalk.dto.responseDto.chatMessageResponseDto;

import com.dalk.domain.ChatMessage;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import lombok.Data;

@Data
public class ChatMessageExitResponseDto {
    private ChatMessage.MessageType type;
    private String roomId;
    private String message;

    public ChatMessageExitResponseDto(ChatMessageRequestDto chatMessageRequestDto) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.message = chatMessageRequestDto.getMessage();
    }
}
