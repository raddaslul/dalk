package com.dalk.domain.wl;

import com.dalk.domain.Comment;
import com.dalk.domain.Timestamped;
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
@Table(name = "warn_comment")
public class WarnComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_warn", nullable = false)
    private Boolean isWarn = false;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}