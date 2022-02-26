package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private Long point;
    private Integer level;
    private User.Role role;
    private ItemResponseDto item;


    public UserInfoResponseDto(String username, String nickname) {
        this.username =username;
        this.nickname=nickname;
        this.point = point;
        this.level = level;
        this.role  = role;
        this.item = item;


    }
}
