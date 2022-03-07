package com.dalk.service;


import com.dalk.domain.*;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnBoardRepository;
import com.dalk.repository.wl.WarnChatRoomRepository;
import com.dalk.repository.wl.WarnCommentRepository;
import com.dalk.security.UserDetailsImpl;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {


    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final ItemRepository itemRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CategoryRepository categoryRepository;
    private final WarnBoardRepository warnBoardRepository;
    private final WarnCommentRepository warnCommentRepository;
    private final WarnChatRoomRepository warnChatRoomRepository;

    //블라인드 게시글 전체 조회 - 관리자

    public List<MainPageBoardResponseDto> getAdminMainPageBoard(UserDetailsImpl userDetails) {

        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {

            //board 전체를 가져옴
            List<Board> boardList = boardRepository.findAll();
            //리턴할 값의 리스트를 정의
            List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

            for (Board board : boardList) {
                List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(board.getId());
                List<Category> categoryList = categoryRepository.findCategoryByBoard(board);
                User user = userRepository.findById(board.getCreateUserId()).orElseThrow(
                        () -> new LoginUserNotFoundException("유저 정보가 없습니다")
                );
                MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(board, MinkiService.categoryStringList(categoryList),user,warnBoardList.size());
                mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
            }
            return mainPageBoardResponseDtoList;
    }
        return null;
    }

//  블라인드 or 게시글  삭제 - 관리자
    public void deleteAdminBoard(Long boardId, UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
            Board board = boardRepository.findById(boardId).orElseThrow(
                    () -> new BoardNotFoundException("해당 게시글이 존재하지 않습니다. ")
            );
            boardRepository.deleteById(board.getId());
        }
    }
        //    토론방 목록 조회 - 관리자

        public List<MainPageAllResponseDto> getAdminMainPageAll (UserDetailsImpl userDetails) {
            if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
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
                    MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, MinkiService.categoryStringList(categoryList), user,warnChatRoomList.size());
                    mainPageAllResponseDtoList.add(mainPageAllResponseDto);
                }
                return mainPageAllResponseDtoList;
            }
            return null;
        }

        //유저 전체 조회 - 관리자
    public List<UserInfoResponseDto> getUserList(UserDetailsImpl userDetails) {

        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
            List<User> userList = userRepository.findAll();
            List<UserInfoResponseDto> allUsers =new ArrayList<>();
            for (User user : userList) {
                UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user);
                allUsers.add(userInfoResponseDto);
            }
            return allUsers;
        }
            return null;
    }

//    유저 삭제 - 관리자
    public void deleteUser(Long userId,UserDetailsImpl userDetails) {
        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
            userRepository.deleteById(userId);
        }
    }

//    채팅방 삭제
//    public void deleteAdminChatRoom(UserDetailsImpl userDetails) {
//        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
//            return null;
//        }
//        return null;
//    }

//



}
