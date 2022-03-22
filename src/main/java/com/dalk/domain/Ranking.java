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
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String nickname;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public Ranking(long rankId, User user) {
        this.id = rankId;
        this.nickname = user.getNickname();
        this.user = user;
    }
}
