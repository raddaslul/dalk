package com.dalk.domain.wl;


import com.dalk.domain.User;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class WarnUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warn_user")
    private String warnUserName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    public WarnUser( User user1 ,String warnUserName) {
        this.user = user1;
        this.warnUserName = warnUserName;
    }
}
