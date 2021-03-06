package com.dalk.dto.responseDto;

import com.dalk.domain.Comment;
import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDto {
    private CreatorInfoResponseDto userInfo;
    private Long commentId;
    private String comment;
    private Integer agreeCnt;
    private Integer disAgreeCnt;
    private Integer WarnCnt;
    private String createdAt;
    private List<Long> warnUserList;
    private List<Long> agreeUserList;
    private List<Long> disagreeUserList;

    public CommentResponseDto(Comment comment, String createdAt, List<Long> warnUserList, List<Long> agreeUserList, List<Long> disagreeUserList) {
        if (comment.getUser() == null) {
            this.userInfo = new CreatorInfoResponseDto(null);
        }
        this.userInfo = new CreatorInfoResponseDto(comment.getUser());
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.agreeCnt = comment.getAgreeCnt();
        this.disAgreeCnt = comment.getDisAgreeCnt();
        this.WarnCnt = warnUserList.size();
        this.createdAt = createdAt;
        this.warnUserList = warnUserList;
        this.agreeUserList = agreeUserList;
        this.disagreeUserList = disagreeUserList;
    }
}
