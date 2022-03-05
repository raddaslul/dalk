package com.dalk.dto.responseDto.chatMessageResponseDto;

import com.dalk.domain.ChatMessage;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageAccessResponseDto {
    private ChatMessage.MessageType type;
    private String roomId;
    private String message;
    private String onlyMe;
    private String myName;

    public ChatMessageAccessResponseDto(ChatMessageRequestDto chatMessageRequestDto) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.message = chatMessageRequestDto.getMessage();
        this.onlyMe = chatMessageRequestDto.getOnlyMe();
        this.myName = chatMessageRequestDto.getMyName();
    }
}
