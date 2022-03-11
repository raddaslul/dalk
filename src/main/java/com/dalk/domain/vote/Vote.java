package com.dalk.domain.vote;

import com.dalk.domain.Board;
import com.dalk.domain.ChatRoom;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vote")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Long topicACnt;

    @Column
    private Long topicBCnt;

    @Column
    private Long topPointA;

    @Column
    private Long topPointB;

    @Column
    private Float totalPointA;

    @Column
    private Float totalPointB;

    @OneToOne
    private ChatRoom chatRoom;

    @OneToOne
    private Board board;

    public Vote(ChatRoom chatRoom) {
        this.topicACnt =0L;
        this.topPointA = 0L;
        this.totalPointA = 0F;
        this.topicBCnt =0L;
        this.topPointB = 0L;
        this.totalPointB = 0F;
        this.chatRoom = chatRoom;
    }

    public Vote(Board board, Long topicACnt, Long topPointA, Float totalPointA,Long topicBCnt, Long topPointB, Float totalPointB) {
        this.topicACnt =topicACnt;
        this.topPointA = topPointA;
        this.totalPointA = totalPointA;
        this.topicBCnt =topicBCnt;
        this.topPointB = topPointB;
        this.totalPointB = totalPointB;
        this.board = board;
    }

}
