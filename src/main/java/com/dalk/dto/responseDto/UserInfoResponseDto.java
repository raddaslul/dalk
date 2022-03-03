package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private Long point;
    private Integer ex;
    private User.Role role;
    private ItemResponseDto item;

    public UserInfoResponseDto(User user, ItemResponseDto itemResponseDto) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.point = user.getPoint();
        this.ex = user.getLevel();
        this.role = user.getRole();
        this.item = itemResponseDto;
    }
}
