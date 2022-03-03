package com.dalk.service;


import com.dalk.domain.*;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.*;
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


    //블라인드 게시글 전체 조회 - 관리자

    public List<MainPageBoardResponseDto> getAdminMainPageBoard(UserDetailsImpl userDetails) {

        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {

            //board 전체를 가져옴
            List<Board> boardList = boardRepository.findAll();
            //리턴할 값의 리스트를 정의
            List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

            for (Board board : boardList) {
                MainPageBoardResponseDto mainPageBoardResponseDto = mainPageBoardResponse(board);
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
                    () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. ")
            );

//        if(!board.getUser().getRole().equals(User.Role.ADMIN)){
//            throw new IllegalArgumentException("관리자만 삭제할 수 있습니다.");
//        }
            boardRepository.deleteById(boardId);
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
                    MainPageAllResponseDto mainPageAllResponseDto = mainPageAllResponse(chatRoom);
                    mainPageAllResponseDtoList.add(mainPageAllResponseDto);
                }
                return mainPageAllResponseDtoList;
            }
            return null;
        }

        //유저 전체 조회 - 관리자
    public List<UserInfoResponseDto> getUserList(UserDetailsImpl userDetails) {

        if (userDetails.getUser().getRole().equals(User.Role.ADMIN)) {
            User user = userDetails.getUser();
            List<User> users = userRepository.findAll();
            List<UserInfoResponseDto> allUsers =new ArrayList<>();
            List<ItemResponseDto> items = new ArrayList<>();
            for (ItemResponseDto itemResponseDto : items) {
                Item item = itemRepository.findByUser(user);
                String itemName = item.getItemName();
                Integer quantity = item.getQuantity();
                itemResponseDto = new ItemResponseDto(itemName,quantity );
                items.add(itemResponseDto);
            }

            Point point = pointRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId()  );
            for (User user1 : users){
                allUsers.add(new UserInfoResponseDto(
                        user1,point,items
                ));
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



    private MainPageAllResponseDto mainPageAllResponse(ChatRoom chatRoom) {
        User user = chatRoom.getUser();
        Point point = pointRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());

        List<ItemResponseDto> items = new ArrayList<>();
        for (ItemResponseDto itemResponseDto : items) {
            Item item = itemRepository.findByUser(user);
            String itemName = item.getItemName();
            Integer quantity = item.getQuantity();
            itemResponseDto = new ItemResponseDto(itemName, quantity);
            items.add(itemResponseDto);
        }
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user, point, items);
        return new MainPageAllResponseDto(chatRoom, userInfoResponseDto);
    }

    private MainPageBoardResponseDto mainPageBoardResponse(Board board) {
        User user = board.getUser();
        Point point = pointRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());
        List<ItemResponseDto> items = new ArrayList<>();
        for (ItemResponseDto itemResponseDto : items) {
            Item item = itemRepository.findByUser(user);
            String itemName = item.getItemName();
            Integer quantity = item.getQuantity();
            itemResponseDto = new ItemResponseDto(itemName, quantity);
            items.add(itemResponseDto);
        }
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user, point, items);
        return new MainPageBoardResponseDto(board, userInfoResponseDto);
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
