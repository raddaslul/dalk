package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.domain.wl.WarnBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "board")
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "topic_a", nullable = false)
    private String topicA;

    @Column(name = "topic_b", nullable = false)
    private String topicB;

    @Column(name = "winner")
    private String winner;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long createUserId;

    @OneToMany(mappedBy = "board")
    private List<Category> categorys;

//    @OneToMany(mappedBy = "board", orphanRemoval = true)
//    private List<WarnBoard> warnBoards = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Board(ChatRoom chatRoom) {
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
//        this.winner = vote.getWinner();
        this.content = chatRoom.getContent();
        this.createUserId = chatRoom.getCreateUserId();
        this.categorys = chatRoom.getCategorys();
    }

    public Board(
            String topicA,
            String topicB,
            String winner,
            String content,
            Long userId){
        this.topicA = topicA;
        this.topicB = topicB;
        this.winner = winner;
        this.content = content;
        this.createUserId = userId;
    }
}