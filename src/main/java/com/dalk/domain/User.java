package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {

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

//    @Column(name = "point")
//    private Long point;

    @Column(name = "level")
    private Integer level;

    @Column
    @Enumerated(value = EnumType.STRING) // 정보를 받을 때는 Enum 값으로 받지만
    // db에 갈때는 Spring Jpa에 의해 자동으로 String으로 변환됨
    private Role role;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private Item item;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Points> points;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
//유저안에는 포인트칼럼이 잇으면안되고, 포인트안에 유저칼럼이있어ㅑㅇ한다.

    public User(
            String username,
            String password,
            String nickname,
            Integer level,
//            Long point,
            Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.level = level;
        this.role = role;
//        this.point = point;
    }
}