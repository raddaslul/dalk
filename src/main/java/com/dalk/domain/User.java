package com.dalk.domain;

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
@Table(name = "user")
public class User extends Timestamped {


    public enum Role {
        ADMIN, USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "point")
    private Long point;

    @Column(name = "level")
    private Integer level;

    @Column(name = "role")
    private Role role;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private Item item;


    public User(String username, String password, String nickname) {

        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}