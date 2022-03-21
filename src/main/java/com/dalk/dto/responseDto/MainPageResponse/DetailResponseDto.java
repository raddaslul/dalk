package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.Board;
import com.dalk.domain.User;
import com.dalk.domain.time.TimeConversion;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailResponseDto {
    private UserInfoResponseDto userInfo;
    private Long boardId;
    private String topicA;
    private String topicB;
    private String winner;
    private List<String> category;
    private String createdAt;
    private Integer commentCnt;
    private Integer warnCnt;
    private List<Long> warnUserList;
    private VoteResultResponseDto winnerResponse;
    private VoteResultResponseDto loserResponse;

    public DetailResponseDto(Board board, List<String> categoryList, User user, Integer warnBoard, List<Long> warnUserList, VoteResultResponseDto winner, VoteResultResponseDto loser) {
        this.userInfo = new UserInfoResponseDto(user);
        this.boardId = board.getId();
        this.topicA = board.getTopicA();
        this.topicB = board.getTopicB();
        this.winner = board.getWinner();
        this.category = categoryList;
        this.createdAt = TimeConversion.timeCreatedConversion(board.getCreatedAt());
        this.commentCnt = board.getComments().size();
        this.warnCnt = warnBoard;
        this.warnUserList = warnUserList;
        this.winnerResponse = winner;
        this.loserResponse = loser;
    }
}
