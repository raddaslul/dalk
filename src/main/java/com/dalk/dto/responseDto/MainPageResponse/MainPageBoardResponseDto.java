package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainPageBoardResponseDto {
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
