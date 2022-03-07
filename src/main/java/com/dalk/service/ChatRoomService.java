package com.dalk.service;

import com.dalk.domain.Board;
import com.dalk.domain.Category;
import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.requestDto.ChatRoomRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnRoomResponseDto;
import com.dalk.exception.ex.ChatRoomNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.CategoryRepository;
import com.dalk.repository.ChatRoomRepository;
import com.dalk.repository.UserRepository;
import com.dalk.repository.wl.WarnChatRoomRepository;
import com.dalk.scheduler.ChatRoomScheduler;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRoomScheduler chatRoomScheduler;
    private final UserRepository userRepository;
    private final WarnChatRoomRepository warnChatRoomRepository;

    public Long createChatRoom(UserDetailsImpl userDetails, ChatRoomRequestDto requestDto) {
        User user = userDetails.getUser();
        Long userId = user.getId();
        ChatRoom chatRoom = new ChatRoom(requestDto, userId);
        chatRoom.setStatus(true);
        chatRoomRepository.save(chatRoom);
        List<String> categoryList = requestDto.getCategory();
        for (String stringCategory : categoryList) {
            Category category = new Category(chatRoom, stringCategory);
            categoryRepository.save(category);
        }
        try{
            return chatRoom.getId();
        } catch (IllegalArgumentException ignored){
        } finally {
            chatRoomScheduler.autoRoomFalse();
        } return null;
    }

    //토론방리스트 탑6 조회
    public List<MainPageAllResponseDto> getMainPageTop6() {
        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findTop6ByStatusOrderByCreatedAtDesc(true);
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

    //토론방리스트 전체조회
    public List<MainPageAllResponseDto> getMainPageAll() {

        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByStatusOrderByCreatedAtDesc(true);
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            List<Category> categoryList = categoryRepository.findCategoryByChatRoom(chatRoom);
            User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom,MinkiService.categoryStringList(categoryList), user,warnChatRoomList.size());
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    //채팅방 하나 입장
    public MainPageAllResponseDto getMainPageOne(Long roomId) {
        List<Category> categoryList = categoryRepository.findCategoryByChatRoom_Id(roomId);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                ()-> new ChatRoomNotFoundException("채팅방이 없습니다.")
        );
        User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                () -> new LoginUserNotFoundException("유저 정보가 없습니다")
        );
        List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
        return new MainPageAllResponseDto(chatRoom, MinkiService.categoryStringList(categoryList), user,warnChatRoomList.size());
    }

    //카테고리 검색
    public List<MainPageAllResponseDto> getSearchCategory(String category) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(category, category, category);
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            List<Category> categoryList = chatRoom.getCategorys();
            User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, MinkiService.categoryStringList(categoryList), user,chatRoomList.size());
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

//    토론방 신고기능
    @Transactional
    public WarnRoomResponseDto WarnChatRoom(Long roomId, UserDetailsImpl userDetails) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                ()-> new ChatRoomNotFoundException("채팅방이 없습니다.")
        );
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new LoginUserNotFoundException("유저 정보가 없습니다")
        );
        WarnRoomResponseDto warnRoomResponseDto =new WarnRoomResponseDto();
        WarnChatRoom warnChatRoomCheck = warnChatRoomRepository.findByUserIdAndChatRoomId(user.getId(),chatRoom.getId() ).orElse(null);

        if (warnChatRoomCheck == null){
            WarnChatRoom warnChatRoom = new WarnChatRoom(true,chatRoom,user);
            warnChatRoomRepository.save(warnChatRoom);
            warnRoomResponseDto.setWarn(warnChatRoom.getIsWarn());
            warnRoomResponseDto.setRoomId(warnChatRoom.getChatRoom().getId());
            return warnRoomResponseDto;
        }
        return null;
    }

//        //카테고리별 채팅방 조회
//    public List<MainPageAllResponseDto> getSearchCategory2(String category) {
//        List<ChatRoom> chatRoomList = chatRoomRepository.findByCategory(category);
//        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();
//
//        for (ChatRoom chatRoom : chatRoomList) {
//            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom);
//            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
//        }
//        return mainPageAllResponseDtoList;
//    }
}
