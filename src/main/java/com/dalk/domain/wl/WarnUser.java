package com.dalk.domain.wl;


import com.dalk.domain.User;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "warn_user")
public class WarnUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "warn_user")
    private String warnUserName;

    @OneToOne
    @JoinColumn
    private User user;

    public WarnUser( User user1 ,String warnUserName) {
        this.user = user1;
        this.warnUserName = warnUserName;
    }
}
