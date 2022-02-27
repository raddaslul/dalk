package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private Long point;
    private Integer level;
    private String role;
    private ItemResponseDto item;

    public UserInfoResponseDto (User user, ItemResponseDto itemResponseDto) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.point = user.getPoint();
        this.level = user.getLevel();
        this.role = user.getRole().toString();
        this.item = itemResponseDto;
    }
}
