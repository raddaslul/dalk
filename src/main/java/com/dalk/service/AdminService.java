package com.dalk.service;


import com.dalk.domain.*;
import com.dalk.domain.vote.SaveVote;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.domain.wl.WarnComment;
import com.dalk.domain.wl.WarnUser;
import com.dalk.dto.requestDto.GivePointRequestDto;
import com.dalk.dto.responseDto.WarnResponse.WarnBoardResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnChatRoomResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnCommentResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnUserResponseDto;
import com.dalk.exception.ex.*;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class AdminService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final S3Repository s3Repository;
    private final VoteRepository voteRepository;
    private final PointRepository pointRepository;
    private final SaveVoteRepository saveVoteRepository;
    private final WarnUserRepository warnUserRepository;
    private final CommentRepository commentRepository;

    //블라인드 게시글 전체 조회 - 관리자
    @Transactional(readOnly = true)
    public List<WarnBoardResponseDto> getAdminMainPageBoard() {

        //board 전체를 가져옴
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        //리턴할 값의 리스트를 정의
        List<WarnBoardResponseDto> warnBoardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            List<WarnBoard> warnBoardList = board.getWarnBoards();
            WarnBoardResponseDto warnBoardResponseDto = new WarnBoardResponseDto(board, warnBoardList.size());
            if (warnBoardList.size() >= 1) {
                warnBoardResponseDtoList.add(warnBoardResponseDto);
            }
        }
        return warnBoardResponseDtoList;
    }

    // 블라인드 or 게시글  삭제 - 관리자
    @Transactional
    public Map<String, Object> deleteAdminBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("해당 게시글이 존재하지 않습니다. ")
        );
        String deleteFileUrl = "image/" + board.getConvertedFileName();
        s3Repository.deleteFile(deleteFileUrl);
        boardRepository.deleteById(board.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }

    // 신고 댓글 목록 조회
    public List<WarnCommentResponseDto> getAdminComment() {
        List<Comment> commentList = commentRepository.findAll();
        List<WarnCommentResponseDto> warnCommentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {

            List<WarnComment> warnComments = comment.getWarnCommentList();
            WarnCommentResponseDto warnCommentResponseDto = new WarnCommentResponseDto(comment,warnComments.size());
            if(warnComments.size()>=1){
                warnCommentResponseDtoList.add(warnCommentResponseDto);
            }
        }

        return warnCommentResponseDtoList;
    }

    // 신고 댓글 삭제
    public Map<String, Object> deleteAdminComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new CommentNotFoundException("해당 댓글이 존재하지 않습니다.")
        );
        commentRepository.deleteById(comment.getId());
        Map<String,Object> result = new HashMap<>();
        result.put("result",true);
        return result;
    }

    // 토론방 목록 조회 - 관리자
    @Transactional(readOnly = true)
    public List<WarnChatRoomResponseDto> getAdminMainPageAll() {
        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByOrderByCreatedAtDesc();
        //리턴할 값의 리스트를 정의
        List<WarnChatRoomResponseDto> warnChatRoomResponseDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            List<WarnChatRoom> warnChatRoomList = chatRoom.getWarnChatRooms();
            WarnChatRoomResponseDto warnChatRoomResponseDto = new WarnChatRoomResponseDto(chatRoom, warnChatRoomList.size());
            if (warnChatRoomList.size() >= 1) {
                warnChatRoomResponseDtoList.add(warnChatRoomResponseDto);
            }
        }
        return warnChatRoomResponseDtoList;
    }

    // 토론방 삭제
    @Transactional
    public Map<String, Object> deleteAdminChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new ChatRoomNotFoundException("토론방이 없습니다."));
        Vote vote = voteRepository.findByChatRoom_Id(chatRoom.getId());
        List<SaveVote> saveVoteReturnList = saveVoteRepository.findAllByChatRoom_Id(roomId);
        List<User> pointReturnList = new ArrayList<>();

        for (SaveVote saveVote : saveVoteReturnList) {
            pointReturnList.add(saveVote.getUser());
        }
        for (User user : pointReturnList) {
            SaveVote saveVote = saveVoteRepository.findByUser_IdAndChatRoom_Id(user.getId(), roomId); //유저와 채팅방 id로 savevote를 뽑아옴 (유저는 한개씩 가짐)
            user.totalPointAdd(saveVote.getPoint());
            userRepository.save(user);
            Point point = new Point("채팅방 삭제", (saveVote.getPoint()), user); //포인트 내역 생성
            pointRepository.save(point);
        }
        vote.setChatRoom(null);
        voteRepository.save(vote);
        voteRepository.delete(vote);
        String deleteFileUrl = "image/" + chatRoom.getConvertedFileName();
        s3Repository.deleteFile(deleteFileUrl);
        chatRoomRepository.delete(chatRoom);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }

    // 유저 신고 조회 - 관리자
    @Transactional(readOnly = true)
    public List<WarnUserResponseDto> getUserList() {
        List<User> userList = userRepository.findAllByOrderByCreatedAtDesc();

        List<WarnUserResponseDto> warnUserResponseDtoList = new ArrayList<>();
        for (User user : userList) {
            List<WarnUser> warnUserList = warnUserRepository.findAllByWarnUserName(user.getUsername());
            WarnUserResponseDto warnUserResponseDto = new WarnUserResponseDto(user, warnUserList.size());
            if(warnUserList.size() >= 1) {
                warnUserResponseDtoList.add(warnUserResponseDto);
            }
        }
        return warnUserResponseDtoList;
    }

    // 신고 유저 삭제 - 관리자
    @Transactional
    public Map<String, Object> deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        ChatRoom chatRoom = chatRoomRepository.findByUser_Id(userId);
        if (chatRoom != null) {
            chatRoom.setUser(null);
            chatRoomRepository.save(chatRoom);
        }
        Board board = boardRepository.findByUser_Id(userId);
        if (board != null) {
            board.setUser(null);
            boardRepository.save(board);
        }
        userRepository.deleteById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }

    //포인트 지급
    @Transactional
    public Map<String, Object> givePoint(GivePointRequestDto givePointRequestDto) {

        User user = userRepository.findByUsername(givePointRequestDto.getUsername()).orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        user.totalPointAdd(givePointRequestDto.getPoint());
        userRepository.save(user);
        Point point = new Point(givePointRequestDto.getContent(), givePointRequestDto.getPoint(), user);
        pointRepository.save(point);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }
}
