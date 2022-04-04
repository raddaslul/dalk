package com.dalk.domain;

import com.dalk.dto.requestDto.ChatMessageRequestDto;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, EXIT, ITEM, ITEMTIMEOUT
    }

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private MessageType type;

    @Column
    private String roomId;

    @Column
    private String message;

    @Column
    private String createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public void setUser(User user) {
        this.user = user;
    }

    public ChatMessage(ChatMessageRequestDto chatMessageRequestDto, User user, ChatRoom chatRoom) {
        this.type = chatMessageRequestDto.getType();
        this.roomId = chatMessageRequestDto.getRoomId();
        this.message = chatMessageRequestDto.getMessage();
        this.createdAt = chatMessageRequestDto.getCreatedAt();
        this.user = user;
        this.chatRoom = chatRoom;
    }
}