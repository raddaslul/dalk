package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.vote.SaveVote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserVoteResponseDto {
    private Long userPoint;
    private Boolean userPick;

    public UserVoteResponseDto(SaveVote saveVote) {
        this.userPoint = saveVote.getPoint();
        this.userPick = saveVote.getPick();
    }
}
