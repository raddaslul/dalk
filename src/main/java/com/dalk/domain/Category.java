package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long Id;

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
    public Category(Board board, String category) {
        this.board = board;
        this.category = category;
    }

}
