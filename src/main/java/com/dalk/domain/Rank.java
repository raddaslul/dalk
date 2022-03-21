package com.dalk.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String nickname;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public Rank(long rankId,User user) {
        this.id = rankId;
        this.nickname = user.getNickname();
        this.user = user;
    }
}
