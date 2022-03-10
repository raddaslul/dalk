package com.dalk.dto.responseDto.chatMessageResponseDto;

import com.dalk.domain.ChatMessage;
import com.dalk.domain.ChatMessageItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageItemResponseDto {
    private ChatMessage.MessageType type;
    private String roomId;
    private String item;
    private String message;
    private String onlyMe;
    private String myName;
    private String papago;
    private String reverse;

    public ChatMessageItemResponseDto(ChatMessageItem chatMessageItem) {
        this.type = chatMessageItem.getType();
        this.roomId = chatMessageItem.getRoomId();
        this.item = chatMessageItem.getItem();
        this.message = chatMessageItem.getMessage();
        this.onlyMe = chatMessageItem.getOnlyMe();
        this.myName = chatMessageItem.getMyName();
        this.papago = chatMessageItem.getPapago();
        this.reverse = chatMessageItem.getReverse();
    }
}
