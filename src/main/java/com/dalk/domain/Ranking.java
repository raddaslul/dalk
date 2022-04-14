package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nickname;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public void setUserRank(User user) {
        this.nickname = user.getNickname();
        this.user = user;
    }
}
