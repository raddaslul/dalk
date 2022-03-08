package com.dalk.dto.responseDto;

import com.dalk.domain.wl.WarnComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;


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
