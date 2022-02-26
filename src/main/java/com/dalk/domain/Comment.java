package com.dalk.domain;

import com.dalk.Timestamped;
import com.dalk.domain.wl.Likes;
import com.dalk.domain.wl.WarnComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
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

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<WarnComment> warnComments = new ArrayList<>();

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<Likes> likeses = new ArrayList<>();
}