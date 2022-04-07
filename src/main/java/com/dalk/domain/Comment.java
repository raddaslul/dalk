package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.domain.wl.Agree;
import com.dalk.domain.wl.WarnComment;
import com.dalk.dto.requestDto.CommentRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @Column
    private Integer agreeCnt = 0;

    @Column
    private Integer disAgreeCnt = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Agree> agreeList;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<WarnComment> warnCommentList;

    public void setAgreeCnt(Integer agreeCnt) {
        this.agreeCnt = agreeCnt;
    }

    public void setDisAgreeCnt(Integer disAgreeCnt) {
        this.disAgreeCnt = disAgreeCnt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment(CommentRequestDto commentRequestDto, Board board, User user) {
        this.comment = commentRequestDto.getComment();
        this.board = board;
        this.user = user;
    }

    public void update(String comment) {
        this.comment = comment;
    }

}