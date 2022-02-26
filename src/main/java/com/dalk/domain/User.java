package com.dalk.domain;

import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
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


    public UserInfoResponseDto toResponseDto(ItemResponseDto item) {
        return UserInfoResponseDto.builder()
                .id(this.id)
                .username(this.username)
                .nickname(this.nickname)
                .point(this.point)
                .level(this.level)
                .role(this.role)
                .item(item)
                .build();
    }
}