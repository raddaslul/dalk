package com.dalk.dto.responseDto.WarnResponse;

import com.dalk.domain.User;
import lombok.Data;

@Data
public class WarnUserResponseDto {
    private Long userId;
    private String username;
    private String nickname;
    private Integer userWarnCnt;

    public WarnUserResponseDto(User user, Integer userWarnCnt) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.userWarnCnt = userWarnCnt;
    }
}