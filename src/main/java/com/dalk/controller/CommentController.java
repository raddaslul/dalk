package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.dto.responseDto.*;
import com.dalk.dto.responseDto.WarnResponse.WarnCommentResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{boardId}")
    @ApiOperation(value = "코멘트 작성")
    public Map<String, Object> createComment(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl UserDetails) {
        User user = UserDetails.getUser();
        return commentService.createComment(boardId, requestDto, user);
    }

    @GetMapping("/api/comments/{boardId}")
    @ApiOperation(value = "댓글 조회")
    public List<CommentResponseDto> getComment(@PathVariable Long boardId) {
        return commentService.getComment(boardId);
    }

    @PutMapping("/comments/{commentId}")
    @ApiOperation(value = "댓글 수정")
    public Map<String, Object> editComment(@PathVariable Long commentId,
                                           @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.editComment(commentId, requestDto, userDetails);
    }

    @DeleteMapping("/comments/{commentId}")
    @ApiOperation(value = "댓글 삭제")
    public Map<String, Object> deleteComment(@PathVariable Long commentId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails);
    }

    @GetMapping("/agree/{commentId}")
    @ApiOperation(value = "찬성하기")
    public AgreeResponseDto agreeCheck(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return commentService.agreeCheck(commentId,userDetails);
    }

    @GetMapping("/disagree/{commentId}")
    @ApiOperation(value = "반대하기")
    public DisAgreeResponseDto disAgreeCheck(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return commentService.disAgreeCheck(commentId,userDetails);
    }

    @GetMapping("/warnings/comments/{commentId}")
    @ApiOperation(value = "댓글 신고하기")
    public Map<String, Object> warnComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return commentService.warnComment(commentId,userDetails);
    }
}
