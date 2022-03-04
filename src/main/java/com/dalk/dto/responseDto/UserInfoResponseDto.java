package com.dalk.dto.responseDto;

import com.dalk.domain.Point;
import com.dalk.domain.User;

import lombok.*;


import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private Long point;
    private Long ex;
    private User.Role role;
    private List<ItemResponseDto> item;

    public UserInfoResponseDto(User user, Point point, List<ItemResponseDto> item) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.point = point.getResultPoint();
        this.ex = user.getEx();
        this.role = user.getRole();
        this.item = item;
    }



}
