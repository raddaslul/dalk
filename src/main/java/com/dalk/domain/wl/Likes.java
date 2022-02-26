package com.dalk.domain.wl;

import com.dalk.Timestamped;
import com.dalk.domain.Comment;
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
@Table(name = "likes")
public class Likes extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_like", nullable = false)
    private Boolean isLike = false;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}