package com.dalk.service;

import com.dalk.domain.Agree;
import com.dalk.domain.Board;
import com.dalk.domain.Comment;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.dto.responseDto.AgreeResponseDto;
import com.dalk.dto.responseDto.CommentResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.handler.ex.CommentNotFoundException;
import com.dalk.handler.ex.LoginUserNotFoundException;
import com.dalk.repository.AgreeRepository;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.CommentRepository;
import com.dalk.repository.UserRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AgreeRepository agreeRepository;

    //댓글 작성
    @Transactional
    public void createComment(Long boardId, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(boardId).orElseGet(null);
        Comment comment = new Comment(requestDto, user, board);
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
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(comment.getUser());
            CommentResponseDto commentResponseDto = new CommentResponseDto(
                    userInfoResponseDto,
                    comment.getId(),
                    comment.getComment(),
                    comment.getLikeses().size(),
                    false
            );
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    //댓글 수정
    @Transactional
    public HashMap<String, Object> editComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comments = commentRepository.findById(commentId).orElseThrow(
                ()-> new NullPointerException("해당 댓글이 없습니다")
        );
        if (comments.getUser().getId().equals(userDetails.getUser().getId())) {
            comments.update(requestDto.getComment());
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "true");
            return result;
        } else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "사용자가 다릅니다");
            return result;
        }
    }

    //댓글 삭제
    @Transactional
    public HashMap<String, Object> deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comments = commentRepository.findById(commentId).orElseThrow(
                ()-> new NullPointerException("해당 댓글이 없습니다")
        );
        if (comments.getUser().getId().equals(userDetails.getUser().getId())) {
            commentRepository.deleteById(comments.getId());
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "true");
            return result;
        } else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "사용자가 다릅니다");
            return result;
        }
    }

    @Transactional
    public AgreeResponseDto agreeCheck(Long commentId, UserDetailsImpl userDetails) {

        AgreeResponseDto agreeResponseDto = new AgreeResponseDto();

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("댓글이 존재하지 않습니다.")
        );
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
        );
//        agree 자체가 null이 됨.
        Agree agreeCheck = agreeRepository.findByUserAndComment(userDetails.getUser(),comment).orElse(null);

        if (agreeCheck == null){
            Agree agree = new Agree(comment,user,true);
            agreeRepository.save(agree);

            agreeResponseDto.setIsAgree(true);
            agree.setAgreeCnt(agree.getAgreeCnt()+1);
            agreeResponseDto.setAgreeCnt(agree.getAgreeCnt());
        }else {
            if(agreeCheck.getIsAgree() == true){
                agreeCheck.setIsAgree(false);
                agreeResponseDto.setIsAgree(false);
                agreeCheck.setAgreeCnt(agreeCheck.getAgreeCnt()-1);
                agreeResponseDto.setAgreeCnt(agreeCheck.getAgreeCnt());

            }else if(agreeCheck.getIsAgree() == false){
                agreeCheck.setIsAgree(true);
                agreeResponseDto.setIsAgree(true);
                agreeCheck.setAgreeCnt(agreeCheck.getAgreeCnt()+1);
                agreeResponseDto.setAgreeCnt(agreeCheck.getAgreeCnt());
            }
        }
        return agreeResponseDto;
    }
}
