package com.dalk.domain.wl;

import com.dalk.Timestamped;
import com.dalk.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "warn_board")
public class WarnBoard extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_warn", nullable = false)
    private Boolean isWarn = false;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}