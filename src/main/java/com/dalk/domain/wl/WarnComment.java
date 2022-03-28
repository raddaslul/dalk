package com.dalk.domain.wl;

import com.dalk.domain.Comment;
import com.dalk.domain.User;
import com.dalk.domain.time.Timestamped;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "warn_comment")
public class WarnComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
//
//    @Column(name = "is_warn")
//    private Boolean isWarn;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public WarnComment( Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}