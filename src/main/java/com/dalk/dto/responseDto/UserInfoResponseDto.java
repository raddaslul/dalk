package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import com.dalk.security.UserDetailsImpl;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private Long point;
    private Integer level;
    private User.Role role;
    private ItemResponseDto item;



    public UserInfoResponseDto(User user) {
        ItemResponseDto itemResponseDto= new ItemResponseDto(user);
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.point = user.getPoint();
        this.level = user.getLevel();
        this.role = user.getRole();
        this.item = itemResponseDto;
    }
}
