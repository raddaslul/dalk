package com.dalk.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long categoryId;

    @JoinColumn(name = "chatRoomId")
    @ManyToOne
    private ChatRoom chatRoom;

    @JoinColumn(name = "boardId")
    @ManyToOne
    private Board board;

    @Column(nullable = false)
    private String category;

    public Category(ChatRoom chatRoom, String category) {
        this.chatRoom = chatRoom;
        this.category = category;
    }

    public Category(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public Category(Board board, String category) {
        this.board = board;
        this.category = category;
    }
}
