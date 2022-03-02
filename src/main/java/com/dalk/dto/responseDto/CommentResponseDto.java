package com.dalk.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private UserInfoResponseDto userInfo;
    private String Comment;
    private Integer likeCnt;
    private Boolean isLike;
}
