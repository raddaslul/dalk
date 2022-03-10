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
    private Integer WarnCnt;
    private List<Long> warnUserList;
    private List<Long> agreeUserList;
    private List<Long> disagreeUserList;
}
