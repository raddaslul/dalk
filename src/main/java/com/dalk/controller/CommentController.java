package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 작성
    //게시글 ID로 댓글 작성
    @PostMapping("/comments/{boardId}")
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
}
