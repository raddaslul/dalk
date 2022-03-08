package com.dalk.service;

import com.dalk.domain.wl.Agree;

import com.dalk.domain.Board;
import com.dalk.domain.Comment;
import com.dalk.domain.User;
import com.dalk.domain.wl.WarnComment;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.dto.responseDto.*;
import com.dalk.dto.responseDto.WarnResponse.WarnCommentResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
import com.dalk.exception.ex.CommentNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.wl.AgreeRepository;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.CommentRepository;
import com.dalk.repository.UserRepository;
import com.dalk.repository.wl.WarnCommentRepository;
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
    private final WarnCommentRepository warnCommentRepository;

    //댓글 작성
    @Transactional
    public void createComment(Long boardId, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(boardId).orElseGet(null);
        Long userId = user.getId();
        Comment comment = new Comment(requestDto,board, userId);
        commentRepository.save(comment);
    }
    //댓글 조회
    @Transactional
    public List<CommentResponseDto> getComment(Long boardId) {
        Board boards = boardRepository.findById(boardId).orElseThrow(
                ()-> new BoardNotFoundException("해당 게시글이 없습니다")
        );
//        Optional<Comment> comment = commentRepository.findById(boardId);
        List<Comment> comments = commentRepository.findAllByBoard(boards);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

//        Long commentId = comment.
//        WarnComment warnComment = warnCommentRepository.findById(commentId);




        for (Comment comment : comments) {
//            Optional<WarnComment> warnComment = warnCommentRepository.findById(commentId);
            User user = userRepository.findById(comment.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );


            List<WarnComment> warnCommentList = warnCommentRepository.findByCommentId(comment.getId());
            List<Long> warnUserList = new ArrayList<>();

            Long a;
            for (WarnComment warnComment : warnCommentList) {
                a = warnComment.getUser().getId();
                warnUserList.add(a);
            }

            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user);

            CommentResponseDto commentResponseDto = new CommentResponseDto(
                    userInfoResponseDto,
                    comment.getId(),
                    comment.getComment(),
                    comment.getAgreeCnt(),
                    comment.getDisAgreeCnt(),
                    warnCommentList.size(),
                    warnUserList
            );


            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    //댓글 수정
    @Transactional
    public HashMap<String, Object> editComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comments = commentRepository.findById(commentId).orElseThrow(
                ()-> new CommentNotFoundException("해당 댓글이 없습니다")
        );
        if (comments.getCreateUserId().equals(userDetails.getUser().getId())) {
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
                ()-> new CommentNotFoundException("해당 댓글이 없습니다")
        );
        if (comments.getCreateUserId().equals(userDetails.getUser().getId())) {
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


//  찬성하기 , 찬성하기 취소
    @Transactional
    public AgreeResponseDto agreeCheck(Long commentId, UserDetailsImpl userDetails) {

        AgreeResponseDto agreeResponseDto = new AgreeResponseDto();
        CommentResponseDto commentResponseDto = new CommentResponseDto();

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("댓글이 존재하지 않습니다.")
        );
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
        );
//        agree 자체가 null이 됨.
        Agree agreeCheck = agreeRepository.findByUserAndComment(userDetails.getUser(), comment).orElse(null);
//            if(agreeCheck.getIsDisAgree()==false||agreeCheck.getIsDisAgree()==null){
//            agreeCheck.setAgreeId(true)
//            }

        if (agreeCheck == null) {
            Agree agree = new Agree(comment, user, false, false);
            agreeRepository.save(agree);
            agree.setIsAgree(true);
            agree.setIsDisAgree(false);
            agreeResponseDto.setIsAgree(true);
            comment.setAgreeCnt(comment.getAgreeCnt() + 1);
            commentResponseDto.setAgreeCnt(comment.getAgreeCnt());
        } else {
            //T T 일경우
            if(agreeCheck.getIsDisAgree() && agreeCheck.getIsAgree()) {
                comment.setDisAgreeCnt(comment.getDisAgreeCnt() - 1);
                agreeCheck.setIsAgree(false);
                agreeCheck.setIsDisAgree(false);
                agreeResponseDto.setIsAgree(false);
                comment.setAgreeCnt(comment.getAgreeCnt() - 1);
                commentResponseDto.setAgreeCnt(comment.getAgreeCnt());
                // T F 일경우
            }else if (agreeCheck.getIsDisAgree() && !agreeCheck.getIsAgree()){
                comment.setDisAgreeCnt(comment.getDisAgreeCnt() - 1);
                agreeCheck.setIsAgree(true);
                agreeCheck.setIsDisAgree(false);
                agreeResponseDto.setIsAgree(true);
                comment.setAgreeCnt(comment.getAgreeCnt() + 1);
                commentResponseDto.setAgreeCnt(comment.getAgreeCnt());
                // F T 일 경우
            }else if(!agreeCheck.getIsDisAgree() && agreeCheck.getIsAgree()) {
                agreeCheck.setIsAgree(false);
                agreeResponseDto.setIsAgree(false);
                comment.setAgreeCnt(comment.getAgreeCnt() - 1);
                commentResponseDto.setAgreeCnt(comment.getAgreeCnt());
                // F F 일 경우
            }else if(!agreeCheck.getIsDisAgree() && !agreeCheck.getIsAgree()){
                agreeCheck.setIsAgree(true);
                agreeResponseDto.setIsAgree(true);
                comment.setAgreeCnt(comment.getAgreeCnt() + 1);
                commentResponseDto.setAgreeCnt(comment.getAgreeCnt());
            }
        }

        return agreeResponseDto;
    }

    // 반대하기 , 반대하기 취소
    @Transactional
    public DisAgreeResponseDto disAgreeCheck(Long commentId, UserDetailsImpl userDetails) {

        DisAgreeResponseDto disAgreeResponseDto = new DisAgreeResponseDto();
        CommentResponseDto commentResponseDto = new CommentResponseDto();

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("댓글이 존재하지 않습니다.")
        );
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
        );

        Agree agreeCheck = agreeRepository.findByUserAndComment(userDetails.getUser(),comment).orElse(null);

        if (agreeCheck == null) {
            Agree agree = new Agree(comment, user, false, false);
            agreeRepository.save(agree);
            agree.setIsAgree(false);
            agree.setIsDisAgree(true);
            disAgreeResponseDto.setIsDisAgree(true);
            comment.setDisAgreeCnt(comment.getDisAgreeCnt() + 1);
            commentResponseDto.setDisAgreeCnt(comment.getDisAgreeCnt());
        } else {

            //T T 일경우
            if(agreeCheck.getIsDisAgree() && agreeCheck.getIsAgree()) {
                comment.setAgreeCnt(comment.getAgreeCnt() - 1);
                agreeCheck.setIsAgree(false);
                agreeCheck.setIsDisAgree(false);
                disAgreeResponseDto.setIsDisAgree(false);
                comment.setDisAgreeCnt(comment.getDisAgreeCnt() - 1);
                commentResponseDto.setDisAgreeCnt(comment.getDisAgreeCnt());
                // T F 일경우
            }else if (agreeCheck.getIsDisAgree() && !agreeCheck.getIsAgree()){

                agreeCheck.setIsDisAgree(false);
                disAgreeResponseDto.setIsDisAgree(false);
                comment.setDisAgreeCnt(comment.getDisAgreeCnt() - 1);
                commentResponseDto.setDisAgreeCnt(comment.getDisAgreeCnt());
                // F T 일 경우
            }else if(!agreeCheck.getIsDisAgree() && agreeCheck.getIsAgree()) {
                comment.setAgreeCnt(comment.getAgreeCnt() - 1);
                agreeCheck.setIsAgree(false);
                agreeCheck.setIsDisAgree(true);
                disAgreeResponseDto.setIsDisAgree(true);
                comment.setDisAgreeCnt(comment.getDisAgreeCnt() + 1);
                commentResponseDto.setDisAgreeCnt(comment.getDisAgreeCnt());
                // F F 일 경우
            }else if(!agreeCheck.getIsDisAgree() && !agreeCheck.getIsAgree()){
                agreeCheck.setIsDisAgree(true);
                disAgreeResponseDto.setIsDisAgree(true);
                comment.setDisAgreeCnt(comment.getDisAgreeCnt() + 1);
                commentResponseDto.setDisAgreeCnt(comment.getDisAgreeCnt());
            }
        }

        return disAgreeResponseDto;

    }


//    댓글 신고하기
    @Transactional
    public WarnCommentResponseDto warnComment(Long commentId, UserDetailsImpl userDetails) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("댓글이 존재하지 않습니다.")
        );
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
        );

        WarnCommentResponseDto warnCommentResponseDto = new WarnCommentResponseDto();

        WarnComment warnCommentCheck = warnCommentRepository.findByUserIdAndComment(userDetails.getUser().getId(),comment).orElse(null);

        if (warnCommentCheck == null){
            WarnComment warnComment = new WarnComment(true, comment, user);
            warnCommentRepository.save(warnComment);
        warnCommentResponseDto.setCommentId(warnComment.getComment().getId());
        warnCommentResponseDto.setWarn(warnComment.getIsWarn());
            System.out.println(warnCommentResponseDto);
            return warnCommentResponseDto;
        }else {
//            WarnComment warnComment = new WarnComment(true, comment, user);
//            warnCommentRepository.save(warnComment);
//            warnCommentResponseDto.setCommentId(warnComment.getComment().getId());
//            warnCommentResponseDto.setWarn(warnComment.getIsWarn());
//            return warnCommentResponseDto;
        return null;

        }
    }
}
