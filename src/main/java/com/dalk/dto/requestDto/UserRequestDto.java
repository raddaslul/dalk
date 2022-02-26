package com.dalk.dto.requestDto;

import com.dalk.domain.User;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class UserRequestDto {
    private String username;
    private String password;
    private String passwordCheck;
    private String nickname;

    public User toEntity(User user) {
        return User.builder()
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .point(user.getPoint())
                .level(user.getLevel())
                .role(user.getRole())
                .item(user.getItem())
                .build();
    }
}
