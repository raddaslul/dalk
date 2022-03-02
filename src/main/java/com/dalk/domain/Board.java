package com.dalk.domain;

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

    @Column(name = "winner", nullable = false)
    private String winner;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "category", nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    @OneToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<WarnBoard> warnBoards = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Board(
            String topicA,
            String topicB,
            String winner,
            String content,
            String category,
            User userId){
        this.topicA = topicA;
        this.topicB = topicB;
        this.winner = winner;
        this.content = content;
        this.category = category;
        this.user = userId;
    }
}