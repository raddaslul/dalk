package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.requestDto.ChatRoomRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnRoomResponseDto;
import com.dalk.dto.responseDto.chatMessageResponseDto.ChatMessageRoomResponseDto;
import com.dalk.exception.ex.*;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnChatRoomRepository;
import com.dalk.scheduler.ChatRoomScheduler;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRoomScheduler chatRoomScheduler;
    private final UserRepository userRepository;
    private final WarnChatRoomRepository warnChatRoomRepository;
    private final VoteRepository voteRepository;
    private final S3Repository s3Repository;

    //토론방 만들기
    public Long createChatRoom(MultipartFile multipartFile, UserDetailsImpl userDetails, ChatRoomRequestDto requestDto) throws IOException {
        String convertedFileName = null;
        String filePath = null;
        if(multipartFile != null) {
            String originalFileName = multipartFile.getOriginalFilename();
            convertedFileName = UUID.randomUUID() + originalFileName;
            filePath = s3Repository.upload(multipartFile, convertedFileName);
        }
        User user = userDetails.getUser();
        Long userId = user.getId();
        ChatRoom chatRoom = new ChatRoom(requestDto, userId, convertedFileName, filePath);
        chatRoomRepository.save(chatRoom);
        Vote vote = new Vote(chatRoom);
        voteRepository.save(vote);
        List<String> categoryList = requestDto.getCategory();
        for (String stringCategory : categoryList) {
            Category category = new Category(chatRoom, stringCategory);
            categoryRepository.save(category);
        }
        try{
            return chatRoom.getId();
        } catch (IllegalArgumentException ignored){
        } finally {
            chatRoomScheduler.autoRoomDelete();
        } return null;
    }

    //토론방리스트 탑6 조회
    public List<MainPageAllResponseDto> getMainPageTop6() {
        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findTop6ByOrderByCreatedAtDesc();
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();



        for (ChatRoom chatRoom : chatRoomList) {
            List<Category> categoryList = categoryRepository.findCategoryByChatRoom(chatRoom);
            User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), user,warnChatRoomList.size(),null);
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    //토론방리스트 전체조회
    public List<MainPageAllResponseDto> getMainPageAll(int page,int size) {
        Pageable pageable = PageRequest.of(page,size);
        //board 전체를 가져옴
        Page<ChatRoom> chatRoomList = chatRoomRepository.findAllByOrderByCreatedAtDesc(pageable);
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            List<Category> categoryList = categoryRepository.findCategoryByChatRoom(chatRoom);
            User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom,ItemService.categoryStringList(categoryList), user,warnChatRoomList.size(),null);
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
        ChatRoomUser chatRoomUser = new ChatRoomUser(chatRoom, user);
        chatRoomUserRepository.save(chatRoomUser);
        List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
        List<Long> warnUserList =new ArrayList<>();
        for (WarnChatRoom warnChatRoom : warnChatRoomList){
            warnUserList.add(warnChatRoom.getUser().getId());
        }
        chatRoom.setUserCnt(chatRoom.getUserCnt()+1);
        chatRoomRepository.save(chatRoom);
        return new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), user,warnChatRoomList.size(),warnUserList);
    }

    // 채팅방 입장 시 기존 메세지 조회
    public List<ChatMessageRoomResponseDto> getMessages(Long roomId) {
        List<ChatMessageRoomResponseDto> chatMessageRoomResponseDtoList = new ArrayList<>();
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByRoomId(roomId);
        for (ChatMessage chatMessage : chatMessageList) {
            User user = userRepository.findById(chatMessage.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
            ChatMessageRoomResponseDto chatMessageRoomResponseDto = new ChatMessageRoomResponseDto(user, chatMessage);
            chatMessageRoomResponseDtoList.add(chatMessageRoomResponseDto);
        }
        return chatMessageRoomResponseDtoList;
    }


    //카테고리 검색
    public List<MainPageAllResponseDto> getSearchCategory(String category,int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ChatRoom> chatRoomList = chatRoomRepository.findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(category, category, category ,pageable);
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            List<Category> categoryList = chatRoom.getCategorys();
            User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), user, warnChatRoomList.size(),null);
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }
    //카테고리에서 제일 사람 많은사람
    public MainPageAllResponseDto getCategoryTop1(String category) {
        ChatRoom chatRoom = chatRoomRepository.findTopByCategorys_Category(category);
        List<Category> categoryList = chatRoom.getCategorys();
        User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                () -> new LoginUserNotFoundException("유저 정보가 없습니다")
        );
        List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
        return new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), user, warnChatRoomList.size(),null);
    }

    //카테고리 검색 제목은 안하고 시간순 정렬
    public List<MainPageAllResponseDto> getMainPageCreatedAt(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<ChatRoom> chatRoomList = chatRoomRepository.findDistinctByCategorys_CategoryOrderByCreatedAt(category, pageable);
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            List<Category> categoryList = chatRoom.getCategorys();
            User user = userRepository.findById(chatRoom.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnChatRoom> warnChatRoomList = warnChatRoomRepository.findByChatRoomId(chatRoom.getId());
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), user, warnChatRoomList.size(),null);
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
        else throw new WarnChatRoomDuplicateException("이미 신고한 채팅방입니다.");
    }
}
