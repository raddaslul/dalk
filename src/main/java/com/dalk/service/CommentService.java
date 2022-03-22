package com.dalk.service;

import com.dalk.domain.wl.Agree;

import com.dalk.domain.Board;
import com.dalk.domain.Comment;
import com.dalk.domain.User;
import com.dalk.domain.wl.WarnComment;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.dto.responseDto.*;
import com.dalk.dto.responseDto.WarnResponse.WarnCommentResponseDto;
import com.dalk.exception.ex.*;
import com.dalk.repository.wl.AgreeRepository;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.CommentRepository;
import com.dalk.repository.UserRepository;
import com.dalk.repository.wl.WarnCommentRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

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

        //생성일자
//        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        Date date = cal.getTime();
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//        String dateResult = sdf.format(date);

        Board boards = boardRepository.findById(boardId).orElseThrow(
                ()-> new BoardNotFoundException("해당 게시글이 없습니다")
        );
        List<Comment> comments = commentRepository.findAllByBoard_Id(boards.getId());
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();




//
        for (Comment comment : comments) {
            User user = userRepository.findById(comment.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );

            String rawCreatedAt = String.valueOf(comment.getCreatedAt());
            String createdAtDate = rawCreatedAt.split("T")[0];
            String createdAtTime = rawCreatedAt.split("T")[1].split("\\.")[0];
            String createdAt = createdAtDate +" "+ createdAtTime;

//          댓글 찬성 , 반대
            List<Agree> agreeList = agreeRepository.findByCommentId(comment.getId());
            List<Agree> disagreeList = agreeRepository.findByCommentId(comment.getId());
            List<Long> agreeUserList = new ArrayList<>();
            List<Long> disagreeUserList =new ArrayList<>();
            for (Agree agree : agreeList){
                if (agree.getIsAgree()){
                    agreeUserList.add(agree.getUser().getId());
                }
            }
            for (Agree agree : disagreeList){
                if (agree.getIsDisAgree())
                disagreeUserList.add(agree.getUser().getId());
            }

//          댓글 신고
            List<WarnComment> warnCommentList = warnCommentRepository.findByCommentId(comment.getId());
            List<Long> warnUserList = new ArrayList<>();

            for (WarnComment warnComment : warnCommentList) {
                warnUserList.add(warnComment.getUser().getId());
            }

            CommentResponseDto commentResponseDto = new CommentResponseDto(user,comment,createdAt,warnUserList,agreeUserList,disagreeUserList);
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
//      agree 자체가 null이 됨.
        Agree agreeCheck = agreeRepository.findByUserAndComment(userDetails.getUser(), comment).orElse(null);

        if (agreeCheck == null) {
            agreeCheck = new Agree(comment, user, true, false);
            agreeResponseDto.setIsAgree(true);
            comment.setAgreeCnt(comment.getAgreeCnt() + 1);
            commentResponseDto.setAgreeCnt(comment.getAgreeCnt());
        } else {
                if (agreeCheck.getIsDisAgree() && !agreeCheck.getIsAgree()){
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
        agreeRepository.save(agreeCheck);
        commentRepository.save(comment);
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
            agreeCheck = new Agree(comment, user, false, true);
            agreeCheck.setIsAgree(false);
            disAgreeResponseDto.setIsDisAgree(true);
            comment.setDisAgreeCnt(comment.getDisAgreeCnt() + 1);
            commentResponseDto.setDisAgreeCnt(comment.getDisAgreeCnt());
        } else {
                if (agreeCheck.getIsDisAgree() && !agreeCheck.getIsAgree()){
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
        agreeRepository.save(agreeCheck);
        commentRepository.save(comment);
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
        }
        else throw new WarnCommentDuplicateException("이미 신고한 댓글입니다.");

    }
}
