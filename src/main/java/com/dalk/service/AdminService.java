//package com.dalk.service;
//
//
//import com.dalk.domain.Board;
//import com.dalk.domain.User;
//import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
//import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
//import com.dalk.dto.responseDto.UserInfoResponseDto;
//import com.dalk.repository.BoardRepository;
//import com.dalk.repository.UserRepository;
//import com.dalk.security.UserDetailsImpl;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@AllArgsConstructor
//public class AdminService {
//
//
//    private final BoardRepository boardRepository;
//    private final UserRepository userRepository;
//
//    //블라인드 게시글 전체 조회 - 관리자
//
//    public List<MainPageBoardResponseDto> getAdminMainPageBoard(UserDetailsImpl userDetails) {
//
//        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
//
//            //board 전체를 가져옴
//            List<Board> boardList = boardRepository.findAll();
//            //리턴할 값의 리스트를 정의
//            List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();
//
//            for (Board boards : boardList) {
//
//                if (boards.getWarnBoards().size() > 5) {
//                    UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(boards.getUser());
//                    MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(
//                            userInfoResponseDto,
//                            boards.getId(),
//                            boards.getTopicA(),
//                            boards.getTopicB(),
//                            boards.getWinner(),
//                            boards.getContent(),
//                            boards.getCategory(),
//                            boards.getCreatedAt(),
//                            boards.getComments().size(),
//                            boards.getWarnBoards().size()
//                    );
//                    mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
//                }
//            }
//
//        return mainPageBoardResponseDtoList;
//    }
//        return null;
//    }
//
////  블라인드 or 게시글  삭제 - 관리자
//    public void deleteAdminBoard(Long boardId, UserDetailsImpl userDetails) {
//        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
//            Board board = boardRepository.findById(boardId).orElseThrow(
//                    () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. ")
//            );
//
////        if(!board.getUser().getRole().equals(User.Role.ADMIN)){
////            throw new IllegalArgumentException("관리자만 삭제할 수 있습니다.");
////        }
//            boardRepository.deleteById(boardId);
//        }
//    }
//        //    토론방 목록 조회 - 관리자
//
//        public MainPageAllResponseDto getAdminMainPageAll (UserDetailsImpl userDetails) {
//            if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
//                return new MainPageAllResponseDto();
//            }
//            return null;
//        }
//
//        //유저 전체 조회 - 관리자
//    public List<UserInfoResponseDto> getUserList(UserDetailsImpl userDetails) {
//
//        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
//
//            List<User> users = userRepository.findAll();
//
//            List<UserInfoResponseDto> allUsers =new ArrayList<>();
//
//            for (User user : users){
//                allUsers.add(new UserInfoResponseDto(
//                        user.getId(),
//                        user.getNickname(),
//                        user.getLevel()
//                ));
//            }
//            return allUsers;
//        }
//            return null;
//    }
//
//    public void deleteUser(Long userId,UserDetailsImpl userDetails) {
//        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
//            userRepository.deleteById(userId);
//        }
//    }
//
//
//
//
////    채팅방 삭제
////    public void deleteAdminChatRoom(UserDetailsImpl userDetails) {
////        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
////            return null;
////        }
////        return null;
////    }
//
////
//
//
//
//}
