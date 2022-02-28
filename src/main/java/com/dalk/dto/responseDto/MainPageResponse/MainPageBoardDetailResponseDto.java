package com.dalk.dto.responseDto.MainPageResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainPageBoardDetailResponseDto {
    //    private UserInfoResponseDto userInfo;
    private Long boardId;
    private String topicA;
    private String topicB;
    private String winner;
    private String content;
    private String category;
    private LocalDateTime createdAt;
    private Integer commentCnt;
    private Integer warnCnt;
}
