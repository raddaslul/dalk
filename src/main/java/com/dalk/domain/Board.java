package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "board")
//@Table(indexes= @Index(name = "board", columnList = "id"))
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

    @Column
    private String convertedFileName;

    @Column
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<WarnBoard> warnBoards;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Category> categorys;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "board",cascade = CascadeType.REMOVE)
    private Vote vote;

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Board(ChatRoom chatRoom) {
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
        this.user = chatRoom.getUser();
        this.categorys = chatRoom.getCategorys();
        this.filePath = chatRoom.getFilePath();
        this.convertedFileName = chatRoom.getConvertedFileName();
    }

    public Board(
            String topicA,
            String topicB,
            String winner,
            User user){
        this.topicA = topicA;
        this.topicB = topicB;
        this.winner = winner;
        this.user = user;
    }
}