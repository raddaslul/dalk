//package com.dalk.service;
//
//import com.dalk.domain.Board;
//import com.dalk.domain.Comment;
//import com.dalk.domain.User;
//import com.dalk.dto.requestDto.CommentRequestDto;
//import com.dalk.repository.BoardRepository;
//import com.dalk.repository.CommentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//
//@RequiredArgsConstructor
//@Service
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//    private final BoardRepository boardRepository;
//
//    //댓글 작성
//    @Transactional
//    public void createComment(Long boardId, CommentRequestDto commentRequestDto, User user) {
//        Board board = boardRepository.findById(boardId).orElseGet(null);
//        Comment comment = new Comment(commentRequestDto, user, board);
//        commentRepository.save(comment);
//    }
//}
