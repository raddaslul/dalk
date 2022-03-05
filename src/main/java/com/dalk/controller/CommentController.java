package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.dto.responseDto.AgreeResponseDto;
import com.dalk.dto.responseDto.CommentResponseDto;
import com.dalk.dto.responseDto.DAgreeResponseDto;
import com.dalk.dto.responseDto.DisAgreeResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{boardId}")
    @ApiOperation(value = "게시글 작성")
    public HashMap<String, Object> createComment(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl UserDetails) {
        User user = UserDetails.getUser();
        commentService.createComment(boardId, requestDto, user);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    @GetMapping("/api/comments/{boardId}")
    @ApiOperation(value = "댓글 조회")
    public List<CommentResponseDto> getComment(@PathVariable Long boardId) {
        return commentService.getComment(boardId);
    }

    @PutMapping("/comments/{commentId}")
    @ApiOperation(value = "댓글 수정")
    public HashMap<String, Object> editComment(@PathVariable Long commentId,
                                               @RequestBody CommentRequestDto requestDto,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.editComment(commentId, requestDto, userDetails);
    }

    @DeleteMapping("/comments/{commentId}")
    @ApiOperation(value = "댓글 삭제")
    public HashMap<String, Object> deleteComment(@PathVariable Long commentId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails);
    }

    //    Agree 찬성하기
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

//    @GetMapping("/dagree/{commentId}")
//    public DAgreeResponseDto




}
