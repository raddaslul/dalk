package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.dto.requestDto.CommentRequestDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "comment")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(nullable = false)
    private Long createUserId;

    @Column
    private Integer agreeCnt = 0;

    @Column
    private Integer disAgreeCnt = 0;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public void setAgreeCnt(Integer agreeCnt) {
        this.agreeCnt = agreeCnt;
    }

    public void setDisAgreeCnt(Integer disAgreeCnt) {
        this.disAgreeCnt = disAgreeCnt;
    }

    public Comment(CommentRequestDto commentRequestDto, Board board, Long userId) {
        this.comment = commentRequestDto.getComment();
        this.board = board;
        this.createUserId = userId;
    }
    public Comment(String comment,
                   Board boardId,
                   Long userId) {
        this.comment = comment;
        this.board = boardId;
        this.createUserId = userId;
    }

    public void update(String comment) {
        this.comment = comment;
    }
}