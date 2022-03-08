package com.dalk.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDto {
    private UserInfoResponseDto userInfo;
    private Long commentId;
    private String comment;
    private Integer agreeCnt;
    private Integer disAgreeCnt;
    private Integer WarnCnts;
    private List<Long> warnUserList;
    private List<Long> agreeUserList;
    private List<Long> disagreeUserList;




//    public CommentResponseDto(UserInfoResponseDto userInfoResponseDto, Long id, String comment, Integer agreeCnt, Integer disAgreeCnt, List<WarnComment> WarnCnts) {
//        this.userInfo = userInfoResponseDto;
//        this.commentId=id;
//        this.comment=comment;
//        this.agreeCnt=agreeCnt;
//        this.disAgreeCnt=disAgreeCnt;
//        this.WarnCnts = WarnCnts;
//
//    }


//    public CommentResponseDto(UserInfoResponseDto userInfoResponseDto, Long id, String comment, Integer agreeCnt, Integer disAgreeCnt) {
//    this.userInfo = userInfoResponseDto;
//    this.commentId=id;
//    this.comment=comment;;
//    this.agreeCnt=agreeCnt;
//    this.disAgreeCnt=disAgreeCnt;
//    }
}
