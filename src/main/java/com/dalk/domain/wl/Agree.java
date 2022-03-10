package com.dalk.domain.wl;


import com.dalk.domain.Comment;
import com.dalk.domain.User;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Agree {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long Id;

    @Column
    private Boolean isAgree;

    @Column
    private Boolean isDisAgree;

    public void setIsAgree(Boolean isAgree) {
        this.isAgree = isAgree;
    }

    public void setIsDisAgree(Boolean isDisAgree) {
        this.isDisAgree = isDisAgree;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Agree(Comment comment, User user, boolean isAgree,boolean isDisAgree) {
        this.comment = comment;
        this.user = user;
        this.isAgree = isAgree;
        this.isDisAgree = isDisAgree;
    }
}
