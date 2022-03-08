package com.dalk.dto.responseDto;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.repository.PointRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
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

    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.point = user.getTotalPoint();
        this.ex = user.getEx();
        this.role = user.getRole();
        this.item = new ItemResponseDto(user);
    }
}
