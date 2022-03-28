package com.dalk.service;


import com.dalk.domain.*;
import com.dalk.domain.vote.SaveVote;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.requestDto.GivePointRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
import com.dalk.exception.ex.ChatRoomNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.exception.ex.UserNotFoundException;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnBoardRepository;
import com.dalk.repository.wl.WarnChatRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AdminService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CategoryRepository categoryRepository;
    private final WarnBoardRepository warnBoardRepository;
    private final WarnChatRoomRepository warnChatRoomRepository;
    private final S3Repository s3Repository;
    private final VoteRepository voteRepository;
    private final PointRepository pointRepository;
    private final SaveVoteRepository saveVoteRepository;

    //블라인드 게시글 전체 조회 - 관리자
    @Transactional(readOnly = true)
    public List<MainPageBoardResponseDto> getAdminMainPageBoard() {

        //board 전체를 가져옴
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        //리턴할 값의 리스트를 정의
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {

            List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(board.getId());
            List<Category> categoryList = categoryRepository.findCategoryByBoard_Id(board.getId());
            User user = userRepository.findById(board.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(board, ItemService.categoryStringList(categoryList), user, warnBoardList.size(), null);

            if (mainPageBoardResponseDto.getWarnCnt() >= 1) {
                mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
            }
        }
        return mainPageBoardResponseDtoList;
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

    // 토론방 목록 조회 - 관리자
    @Transactional(readOnly = true)
    public List<MainPageAllResponseDto> getAdminMainPageAll() {
        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByOrderByCreatedAtDesc();
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            List<Category> categoryList = categoryRepository.findCategoryByChatRoom(chatRoom);
            User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), user, warnChatRoomList.size(), null);

            if (mainPageAllResponseDto.getWarnCnt() >= 1) {
                mainPageAllResponseDtoList.add(mainPageAllResponseDto);
            }
        }
        return mainPageAllResponseDtoList;
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
    public List<UserInfoResponseDto> getUserList() {

        List<User> userList = userRepository.findAllByOrderByWarnUserCntDesc();
        List<UserInfoResponseDto> allUsers = new ArrayList<>();
        for (User user : userList) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user);
            if (userInfoResponseDto.getWarnUserCnt() >= 1) {
                allUsers.add(userInfoResponseDto);
            }
        }
        return allUsers;
    }

    // 유저 삭제 - 관리자
    @Transactional
    public Map<String, Object> deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        userRepository.deleteById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }

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
