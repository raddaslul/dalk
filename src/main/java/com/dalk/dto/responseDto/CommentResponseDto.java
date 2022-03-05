package com.dalk.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDto {
    private UserInfoResponseDto userInfo;
    private Long commentId;
    private String Comment;
    private Integer likeCnt;
    private Boolean isLike;
}
