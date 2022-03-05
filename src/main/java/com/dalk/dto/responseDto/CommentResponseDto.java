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
    private String comment;
    private Integer agreeCnt;
    private Integer disAgreeCnt;
//    public CommentResponseDto(UserInfoResponseDto userInfoResponseDto, Long id, String comment, Integer agreeCnt, Integer disAgreeCnt) {
//    this.userInfo = userInfoResponseDto;
//    this.commentId=id;
//    this.comment=comment;;
//    this.agreeCnt=agreeCnt;
//    this.disAgreeCnt=disAgreeCnt;
//    }
}
