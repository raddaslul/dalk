package com.dalk.domain;

import com.dalk.domain.time.Timestamped;

import com.dalk.domain.wl.WarnComment;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User extends Timestamped {



    public enum Role {
        ADMIN, USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "level")
    private Long ex;

    @Column
    @Enumerated(value = EnumType.STRING) // 정보를 받을 때는 Enum 값으로 받지만
    // db에 갈때는 Spring Jpa에 의해 자동으로 String으로 변환됨
    private Role role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Item> items;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Point> point;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public User(
            String username,
            String password,
            String nickname,
            Long ex,
            Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.ex = ex;
        this.role = role;
    }
}