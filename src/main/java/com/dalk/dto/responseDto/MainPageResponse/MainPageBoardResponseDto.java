package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.Board;
import com.dalk.domain.time.TimeConversion;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.service.MinkiService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class MainPageBoardResponseDto {
    private UserInfoResponseDto userInfo;
    private Long boardId;
    private String topicA;
    private String topicB;
    private String winner;
    private String content;
    private String category;
    private String createdAt;
    private Integer commentCnt;
    private Integer warnCnt;

    public MainPageBoardResponseDto(Board board) {
//        this.userInfo = new UserInfoResponseDto(board.getUser());
        this.userInfo = MinkiService.userInfo(board.getUser());
        this.boardId = board.getId();
        this.topicA = board.getTopicA();
        this.topicB = board.getTopicB();
        this.winner = board.getWinner();
        this.content = board.getContent();
        this.category = board.getCategory();
        this.createdAt = TimeConversion.timeCreatedConversion(board.getCreatedAt());
        this.commentCnt = board.getComments().size();
        this.warnCnt = board.getWarnBoards().size();
    }
}
