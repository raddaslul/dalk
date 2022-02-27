package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "chat_message")
public class ChatMessage extends Timestamped {

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "type", nullable = false)
    private MessageType type;
}