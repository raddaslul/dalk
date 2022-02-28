package com.dalk.controller;

import com.dalk.config.auth.UserDetailsImpl;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.dto.responseDto.CommentResponseDto;
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
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl UserDetails)
    {
        User user = UserDetails.getUser();
        commentService.createComment(boardId, commentRequestDto, user);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    @GetMapping("/api/comments/{boardId}")
    @ApiOperation(value = "댓글 조회")
    public List<CommentResponseDto> getComment(@PathVariable Long boardId) {
        return commentService.getComment(boardId);
    }
}
