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
    private String item;
    private Boolean bigFont;
    private String onlyMe;
    private String myName;
    private Long userId;
    private String createdAt;
}
