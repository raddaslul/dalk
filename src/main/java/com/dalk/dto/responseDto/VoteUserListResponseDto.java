package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class VoteUserListResponseDto {
    private Long userId;
    private String nickname;

    public VoteUserListResponseDto(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
    }
}
