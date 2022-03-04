package com.dalk.dto.requestDto;

import com.dalk.domain.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type;
    private String roomId;
    private String message;
    private Boolean bigFont;
    private Long userId;
    private String createdAt;
}
