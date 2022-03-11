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
@Table(name = "warn_board")
public class WarnBoard extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_warn")
    private Boolean isWarn;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public WarnBoard(boolean isWarn, Board board, User user) {
        this.isWarn = isWarn;
        this.board = board;
        this.user = user;
    }
}