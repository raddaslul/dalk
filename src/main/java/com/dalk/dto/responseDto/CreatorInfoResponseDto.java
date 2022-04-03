package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class CreatorInfoResponseDto {
    private Long userId;
    private String username;
    private String nickname;
    private Long point;
    private Integer ex;
    private Long rank;
    private Integer lottoCount;
    private User.Role role;

    public CreatorInfoResponseDto(User user) {
        if (user != null) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.point = user.getTotalPoint();
            this.ex = user.getEx();
            if (user.getRanking() != null) {
                this.rank = user.getRanking().getId();
            } else {
                this.rank = null;
            }
            this.lottoCount = user.getLottoCnt();
            this.role = user.getRole();

        } else {
            this.userId = null;
            this.username = "탈퇴한유저";
            this.nickname = "탈퇴한유저";
            this.point = 0L;
            this.ex = 0;
            this.rank =null;
            this.lottoCount = 0;
            this.role = null;
        }
    }
}
