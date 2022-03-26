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
    private Integer lottoCount;
    private User.Role role;
    private ItemResponseDto item;

    public UserInfoResponseDto(User user) {
        if (user != null) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.point = user.getTotalPoint();
            this.ex = user.getEx();
            this.warnUserCnt = user.getWarnUserCnt();
            if (user.getRanking() != null) {
                this.rank = user.getRanking().getId();
            } else {
                this.rank = null;
            }
            this.lottoCount = user.getLottoCnt();
            this.role = user.getRole();
            this.item = new ItemResponseDto(user);

        } else {
            this.userId = null;
            this.username = "탈퇴한유저";
            this.nickname = "탈퇴한유저";
            this.point = 0L;
            this.ex = 0;
            this.warnUserCnt =0;
            this.rank =null;
            this.lottoCount = 0;
            this.role = null;
            this.item = null;
        }
    }
}
