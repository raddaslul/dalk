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
public class ChatMessageEnterResponseDto {
    private ChatMessage.MessageType type;
    private String roomId;
    private String message;
    private String onlyMe;
    private String myName;
    private String papago;
    private String reverse;
    private UserInfoResponseDto userInfo;

    public ChatMessageEnterResponseDto(ChatMessageRequestDto chatMessageRequestDto, User user) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.message = chatMessageRequestDto.getMessage();
        this.onlyMe = chatMessageRequestDto.getOnlyMe();
        this.myName = chatMessageRequestDto.getMyName();
        this.papago = chatMessageRequestDto.getPapago();
        this.reverse = chatMessageRequestDto.getReverse();
        this.userInfo = new UserInfoResponseDto(user);
    }
}
