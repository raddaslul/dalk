package com.dalk.domain.wl;

import com.dalk.domain.Board;
import com.dalk.domain.User;
import com.dalk.domain.time.Timestamped;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class WarnBoard extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public WarnBoard(Board board, User user) {
        this.board = board;
        this.user = user;
    }

    public WarnBoard(WarnChatRoom warnChatRoom, Board board) {
        this.user = warnChatRoom.getUser();
        this.board = board;
    }

}