package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.dto.requestDto.ChatMessageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessageItem extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private ChatMessage.MessageType type;

    @Column
    private String roomId;

    @Column
    private Long userId;

    @Column
    private String item;

    @Column
    private String message;

    @Column
    private String onlyMe;

    @Column
    private String myName;

    @Column
    private String papago;

    @Column
    private String reverse;

    public ChatMessageItem(ChatMessageRequestDto chatMessageRequestDto) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.userId = chatMessageRequestDto.getUserId();
        this.item = chatMessageRequestDto.getItem();
        this.message = chatMessageRequestDto.getMessage();
        this.onlyMe = chatMessageRequestDto.getOnlyMe();
        this.myName = chatMessageRequestDto.getMyName();
        this.papago = chatMessageRequestDto.getPapago();
        this.reverse = chatMessageRequestDto.getReverse();
    }
}
