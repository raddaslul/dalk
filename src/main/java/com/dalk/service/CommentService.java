package com.dalk.service;

import com.dalk.domain.Board;
import com.dalk.domain.Comment;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.dto.responseDto.CommentResponseDto;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 작성
    @Transactional
    public void createComment(Long boardId, CommentRequestDto commentRequestDto, User user) {
        Board board = boardRepository.findById(boardId).orElseGet(null);
        Comment comment = new Comment(commentRequestDto, user, board);
        commentRepository.save(comment);
    }

    //댓글 조회
    @Transactional
    public List<CommentResponseDto> getComment(Long boardId) {
        Board boards = boardRepository.findById(boardId).orElseThrow(
                ()-> new NullPointerException("해당 게시글이 없습니다")
        );
        List<Comment> comments = commentRepository.findAllByBoard(boards);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(
                    comment.getComment(),
                    comment.getLikeses().size(),
                    false
            );
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }
}
