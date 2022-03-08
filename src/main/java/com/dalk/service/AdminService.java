package com.dalk.service;


import com.dalk.domain.*;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.responseDto.ItemResponseDto;
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
import com.dalk.repository.wl.WarnCommentRepository;
import com.dalk.security.UserDetailsImpl;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(board, MinkiService.categoryStringList(categoryList), user, warnBoardList.size(),null);
                mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
            }
            return mainPageBoardResponseDtoList;
        }
        return null;
    }

    //  블라인드 or 게시글  삭제 - 관리자
    public void deleteAdminBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("해당 게시글이 존재하지 않습니다. ")
        );
        boardRepository.deleteById(board.getId());
    }
    //    토론방 목록 조회 - 관리자

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
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, MinkiService.categoryStringList(categoryList), user, warnChatRoomList.size());
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    //    토론방 삭제
    public void deleteAdminChatRoom(Long roomId) {
        chatRoomRepository.findById(roomId).orElseThrow(()-> new ChatRoomNotFoundException("채팅방이 없습니다."));
        chatRoomRepository.deleteById(roomId);
    }

    //유저 전체 조회 - 관리자
    public List<UserInfoResponseDto> getUserList() {

        List<User> userList = userRepository.findAll();
        List<UserInfoResponseDto> allUsers = new ArrayList<>();
        for (User user : userList) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user);
            allUsers.add(userInfoResponseDto);
        }
        return allUsers;
    }

    //    유저 삭제 - 관리자
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        userRepository.deleteById(userId);
    }

    // 메인 배너 등록
    public void uploadFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String convertedFileName = UUID.randomUUID() + originalFileName;
        s3Repository.upload(multipartFile, convertedFileName);
    }


//


}
