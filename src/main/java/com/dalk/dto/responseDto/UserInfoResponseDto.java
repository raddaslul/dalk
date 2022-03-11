package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import com.dalk.domain.wl.WarnUser;
import lombok.*;

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
    private Integer warnUserCnt;
    private Integer rank;
    private User.Role role;
    private ItemResponseDto item;


    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.point = user.getTotalPoint();
        this.ex = user.getEx();
        this.warnUserCnt =user.getWarnUserCnt();
        this.rank = user.getRank();
        this.role = user.getRole();
        this.item = new ItemResponseDto(user);
    }
}
