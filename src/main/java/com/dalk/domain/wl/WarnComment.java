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
public class WarnComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public WarnComment( Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}