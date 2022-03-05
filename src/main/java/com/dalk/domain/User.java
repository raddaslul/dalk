package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Column(name = "totalpoint")
    private Long totalPoint;

    @Column(name = "level")
    private Integer level;

    @Column
    @Enumerated(value = EnumType.STRING) // 정보를 받을 때는 Enum 값으로 받지만
    // db에 갈때는 Spring Jpa에 의해 자동으로 String으로 변환됨
    private Role role;

    @OneToOne
    private Item item;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER)
    private List<Point> points;

    public User(String username, String password, String nickname,Long totalPoint,Integer level, Role role, Item item) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.totalPoint = totalPoint;
        this.level = level;
        this.role = role;
        this.item = item;
    }
}