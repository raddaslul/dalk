package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import lombok.*;

import javax.persistence.*;

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

    @Column(name = "point")
    private Long point;

    @Column(name = "level")
    private Integer level;

    @Column
    @Enumerated(value = EnumType.STRING) // 정보를 받을 때는 Enum 값으로 받지만
    // db에 갈때는 Spring Jpa에 의해 자동으로 String으로 변환됨
    private Role role;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private Item item;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public User(
            String username,
            String password,
            String nickname,
            Long point,
            Integer leve,
            Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.point = point;
        this.level = leve;
        this.role = role;
    }
}