package com.dalk.dto.responseDto.WarnResponse;

import com.dalk.domain.Comment;
import lombok.Data;

@Data
public class WarnCommentResponseDto {
    private Long commentId;
    private String comment;
    private Integer warnCnt;

    public WarnCommentResponseDto (Comment comment , Integer warnCnt){
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.warnCnt = warnCnt;

    }
}
