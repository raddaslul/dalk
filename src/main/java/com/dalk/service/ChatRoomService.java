package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.domain.vote.SaveVote;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.requestDto.ChatRoomRequestDto;
import com.dalk.dto.responseDto.CreatorInfoResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.ChatRoomEnterResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRoomScheduler chatRoomScheduler;
    private final WarnChatRoomRepository warnChatRoomRepository;
    private final VoteRepository voteRepository;
    private final S3Repository s3Repository;
    private final SaveVoteRepository saveVoteRepository;

    //토론방 만들기
    @Transactional
    public Long createChatRoom(MultipartFile multipartFile, UserDetailsImpl userDetails, ChatRoomRequestDto requestDto) throws IOException {
        String convertedFileName = null;
        String filePath = null;
        if (multipartFile != null) {
            String originalFileName = multipartFile.getOriginalFilename();
            convertedFileName = UUID.randomUUID() + originalFileName;
            filePath = s3Repository.upload(multipartFile, convertedFileName);
        }
        User user = userDetails.getUser();
        ChatRoom chatRoom = new ChatRoom(requestDto, user, convertedFileName, filePath);
        chatRoomRepository.save(chatRoom);
        Vote vote = new Vote(chatRoom);
        voteRepository.save(vote);
        List<String> categoryList = requestDto.getCategory();
        for (String stringCategory : categoryList) {
            Category category = new Category(chatRoom, stringCategory);
            categoryRepository.save(category);
        }
        try {
            return chatRoom.getId();
        } catch (IllegalArgumentException ignored) {
        } finally {
            chatRoomScheduler.autoRoomDelete();
        }
        return null;
    }

    //토론방리스트 탑6 조회
    @Transactional
    public List<MainPageAllResponseDto> getMainPageTop6() {
        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findTop6ByOrderByCreatedAtDesc();
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            getCnt(chatRoom);
            List<Category> categoryList = chatRoom.getCategorys();
            List<WarnChatRoom> warnChatRoomList = chatRoom.getWarnChatRooms();
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), warnChatRoomList.size(), null);
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    //토론방리스트 전체조회
    @Transactional
    public List<MainPageAllResponseDto> getMainPageAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        //board 전체를 가져옴
        Page<ChatRoom> chatRoomList = chatRoomRepository.findAllByOrderByCreatedAtDesc(pageable);
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            getCnt(chatRoom);
            List<Category> categoryList = chatRoom.getCategorys();
            List<WarnChatRoom> warnChatRoomList = chatRoom.getWarnChatRooms();
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), warnChatRoomList.size(), null);
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    //채팅방 하나 입장
    @Transactional
    public ChatRoomEnterResponseDto getMainPageOne(Long roomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new ChatRoomNotFoundException("채팅방이 없습니다.")
        );
        List<Category> categoryList = chatRoom.getCategorys();
        getCnt(chatRoom);
        SaveVote saveVote = saveVoteRepository.findByUser_IdAndChatRoom_Id(user.getId(), roomId);
        List<WarnChatRoom> warnChatRoomList = chatRoom.getWarnChatRooms();
        List<Long> warnUserList = new ArrayList<>();
        for (WarnChatRoom warnChatRoom : warnChatRoomList) {
            warnUserList.add(warnChatRoom.getUser().getId());
        }
        if (saveVote == null) {
            return new ChatRoomEnterResponseDto(chatRoom, ItemService.categoryStringList(categoryList), user, warnChatRoomList.size(), warnUserList);
        } else
            return new ChatRoomEnterResponseDto(saveVote, chatRoom, ItemService.categoryStringList(categoryList), user, warnChatRoomList.size(), warnUserList);
    }

    // 채팅방 입장 시 기존 메세지 조회
    @Transactional(readOnly = true)
    public List<ChatMessageRoomResponseDto> getMessages(String roomId) {
        List<ChatMessageRoomResponseDto> chatMessageRoomResponseDtoList = new ArrayList<>();
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByRoomId(roomId);
        for (ChatMessage chatMessage : chatMessageList) {
            ChatMessageRoomResponseDto chatMessageRoomResponseDto = new ChatMessageRoomResponseDto(chatMessage);
            chatMessageRoomResponseDtoList.add(chatMessageRoomResponseDto);
        }
        return chatMessageRoomResponseDtoList;
    }

    // 채팅방 입장시 채팅방에 참여한 유저 목록 조회
    @Transactional(readOnly = true)
    public List<CreatorInfoResponseDto> getUsers(Long roomId) {
        List<CreatorInfoResponseDto> creatorInfoResponseDtoList = new ArrayList<>();
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findAllByChatRoom_Id(roomId);
        for (ChatRoomUser chatRoomUser : chatRoomUserList) {
            User user = chatRoomUser.getUser();
            CreatorInfoResponseDto creatorInfoResponseDto = new CreatorInfoResponseDto(user);
            creatorInfoResponseDtoList.add(creatorInfoResponseDto);
        }
        return creatorInfoResponseDtoList;
    }

    //카테고리 검색
    @Transactional
    public List<MainPageAllResponseDto> getSearchCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoom> chatRoomList = chatRoomRepository.findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCaseOrderByCreatedAtDesc(category, category, category, pageable);
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            getCnt(chatRoom);
            List<Category> categoryList = chatRoom.getCategorys();
            List<WarnChatRoom> warnChatRoomList = chatRoom.getWarnChatRooms();
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), warnChatRoomList.size(), null);
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    //카테고리에서 제일 사람 많은사람
    @Transactional
    public MainPageAllResponseDto getCategoryTop1(String category) {
        ChatRoom chatRoom = chatRoomRepository.findTopByCategorys_CategoryOrderByUserCntDesc(category);
        if (chatRoom == null) {
            return null;
        }
        getCnt(chatRoom);
        List<Category> categoryList = chatRoom.getCategorys();
        List<WarnChatRoom> warnChatRoomList = chatRoom.getWarnChatRooms();
        return new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), warnChatRoomList.size(), null);
    }

    //카테고리 검색 제목은 안하고 시간순 정렬 카테고리 검색시
    @Transactional
    public List<MainPageAllResponseDto> getMainPageCreatedAt(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoom> chatRoomList = chatRoomRepository.findDistinctByCategorys_CategoryOrderByCreatedAtDesc(category, pageable);
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            getCnt(chatRoom);
            List<Category> categoryList = chatRoom.getCategorys();
            List<WarnChatRoom> warnChatRoomList = chatRoom.getWarnChatRooms();
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom, ItemService.categoryStringList(categoryList), warnChatRoomList.size(), null);
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    // 토론방 신고기능
    @Transactional
    public Map<String, Object> WarnChatRoom(Long roomId, UserDetailsImpl userDetails) {
        Map<String, Object> result = new HashMap<>();
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new ChatRoomNotFoundException("채팅방이 없습니다.")
        );
        User user = userDetails.getUser();
        WarnChatRoom warnChatRoomCheck = warnChatRoomRepository.findByUserIdAndChatRoomId(user.getId(), chatRoom.getId()).orElse(null);

        if (warnChatRoomCheck == null) {
            WarnChatRoom warnChatRoom = new WarnChatRoom(chatRoom, user);
            warnChatRoomRepository.save(warnChatRoom);
            result.put("result", true);
            return result;
        }
        //예외 처리안하면 신고두번되니까  500에러가뜸 그런데그런데 예측할 수 있는 에러를 400에러로 바꿈
//        500에러 서버, 400에러 프론트에러 , 500에러 예측할수 없는에러 , 400에러 예측할 수있는 에러
        else throw new WarnChatRoomDuplicateException("이미 신고한 채팅방입니다.");
    }

    private void getCnt(ChatRoom chatRoom) {
        chatRoom.setUserCnt(chatRoom.getChatRoomUser().size());
        chatRoomRepository.save(chatRoom);
    }
}
