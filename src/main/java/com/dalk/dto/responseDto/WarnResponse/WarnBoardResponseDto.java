package com.dalk.dto.responseDto.WarnResponse;

import com.dalk.domain.Board;
import lombok.Data;

@Data
public class WarnBoardResponseDto {
    private Long boardId;
    private String topicA;
    private String topicB;
    private Integer boardWarnCnt;

    public WarnBoardResponseDto(Board board, Integer boardWarnCnt) {
        this.boardId = board.getId();
        this.topicA = board.getTopicA();
        this.topicB = board.getTopicB();
        this.boardWarnCnt = boardWarnCnt;
    }
}
