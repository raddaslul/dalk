package com.dalk.domain.vote;

import com.dalk.domain.ChatRoom;
import com.dalk.dto.requestDto.VoteRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<SaveVote> saveVoteList;

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public Vote(ChatRoom chatRoom) {
        this.topicACnt = 0L;
        this.topPointA = 0L;
        this.totalPointA = 0F;
        this.topicBCnt = 0L;
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
