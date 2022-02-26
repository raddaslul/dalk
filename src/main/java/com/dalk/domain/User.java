package com.dalk.domain;

import com.dalk.Timestamped;
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

    @Column(name = "point", nullable = false)
    private Long point;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private Item item;
}