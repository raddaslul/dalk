package com.dalk.domain;

import com.dalk.config.auth.UserDetailsImpl;
import com.dalk.domain.wl.Likes;
import com.dalk.domain.wl.WarnComment;
import com.dalk.dto.requestDto.CommentRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<WarnComment> warnComments = new ArrayList<>();

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<Likes> likeses = new ArrayList<>();


    public Comment(CommentRequestDto commentRequestDto, User user, Board board) {
        this.comment = commentRequestDto.getComment();
        this.board = board;
        this.user = user;
    }
    public Comment(String comment,
                   Board boardId,
                   User userId) {
        this.comment = comment;
        this.board = boardId;
        this.user = userId;
    }

    public void update(String comment) {
        this.comment = comment;
    }
}