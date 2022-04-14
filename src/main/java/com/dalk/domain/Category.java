package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String category;

    @JoinColumn(name = "chatRoomId")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @JoinColumn(name = "boardId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    public Category(ChatRoom chatRoom, String category) {
        this.chatRoom = chatRoom;
        this.category = category;
    }

    public Category(Board board, String category) {
        this.board = board;
        this.category = category;
    }
}
