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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private Boolean isAgree;

    @Column
    private Boolean isDisAgree;

    public void isAgreeTure() {
        this.isAgree = true;
    }
    public void isDisAgreeTure() {
        this.isDisAgree = true;
    }

    public void isAgreeFalse() {
        this.isAgree = false;
    }

    public void isDisAgreeFalse() {
        this.isDisAgree = false;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Agree(Comment comment, User user, boolean isAgree, boolean isDisAgree) {
        this.comment = comment;
        this.user = user;
        this.isAgree = isAgree;
        this.isDisAgree = isDisAgree;
    }
}
