package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class CreatorInfoResponseDto {
    private Long userId;
    private String username;
    private String nickname;
    private Integer ex;
    private Long rank;

    public CreatorInfoResponseDto(User user) {
        if (user != null) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.ex = user.getEx();
            if (user.getRanking() != null) {
                this.rank = user.getRanking().getId();
            } else {
                this.rank = null;
            }

        } else {
            this.userId = null;
            this.username = "탈퇴한유저";
            this.nickname = "탈퇴한유저";
            this.ex = 0;
            this.rank =null;
        }
    }
}
