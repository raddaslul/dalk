package com.dalk.domain.vote;

import com.dalk.domain.Board;
import com.dalk.domain.ChatRoom;
import com.dalk.dto.requestDto.VoteRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
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
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    @OneToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<SaveVote> saveVoteList;

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Vote(ChatRoom chatRoom) {
        this.topicACnt =0L;
        this.topPointA = 0L;
        this.totalPointA = 0F;
        this.topicBCnt =0L;
        this.topPointB = 0L;
        this.totalPointB = 0F;
        this.chatRoom = chatRoom;
    }
    public void winnerA(VoteRequestDto requestDto) {
        this.totalPointA += requestDto.getPoint();
        this.topicACnt += 1;
        if (this.topPointA < requestDto.getPoint()) {
            this.topPointA = requestDto.getPoint();
        }
    }

    public void winnerB(VoteRequestDto requestDto) {
        this.totalPointB += requestDto.getPoint();
        this.topicBCnt += 1;
        if (this.topPointB < requestDto.getPoint()) {
            this.topPointB = requestDto.getPoint();
        }
    }
}
