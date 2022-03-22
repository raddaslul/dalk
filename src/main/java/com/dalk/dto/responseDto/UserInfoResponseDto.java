package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class UserInfoResponseDto {
    private Long userId;
    private String username;
    private String nickname;
    private Long point;
    private Integer ex;
    private Integer warnUserCnt;
    private Long rank;
    private User.Role role;
    private ItemResponseDto item;

    public UserInfoResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.point = user.getTotalPoint();
        this.ex = user.getEx();
        this.warnUserCnt =user.getWarnUserCnt();
        this.rank = user.getRanking().getId();
        this.role = user.getRole();
        this.item = new ItemResponseDto(user);
    }
}
