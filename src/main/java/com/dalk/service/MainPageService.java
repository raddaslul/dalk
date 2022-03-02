package com.dalk.service;

import com.dalk.domain.Board;
import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.MainPageRequest.CreateChatRoomRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardDetailResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageTop6ResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.ChatRoomRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MainPageService {

    private final BoardRepository boardRepository;
    private final ChatRoomRepository chatRoomRepository;

    //채팅방 생성
    public Long createChatRoom(UserDetailsImpl userDetails, CreateChatRoomRequestDto requestDto) {
        ChatRoom chatRoom = new ChatRoom(userDetails, requestDto);
        chatRoom = chatRoomRepository.save(chatRoom);
        Long chatRoomId = chatRoom.getId();
        return chatRoomId;
    }

    //토론방리스트 탑6 조회
    public List<MainPageTop6ResponseDto> getMainPageTop6() {
        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findTop6ByOrderByCreatedAtTimeDesc();
        //리턴할 값의 리스트를 정의
        List<MainPageTop6ResponseDto> mainPageTop6ResponseDtoList = new ArrayList<>();


        for (ChatRoom chatRoom : chatRoomList) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(chatRoom.getUser());
            MainPageTop6ResponseDto mainPageTop6ResponseDto = new MainPageTop6ResponseDto(
                    userInfoResponseDto,
                    chatRoom.getId(),
                    chatRoom.getTopicA(),
                    chatRoom.getTopicB(),
                    chatRoom.getContent(),
                    chatRoom.getCategory(),
                    "time",
                    chatRoom.getCreatedAt()
            );
            mainPageTop6ResponseDtoList.add(mainPageTop6ResponseDto);
        }
        return mainPageTop6ResponseDtoList;
    }

    //토론방리스트 전체조회
    public List<MainPageAllResponseDto> getMainPageAll() {

        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(chatRoom.getUser());
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(
                    userInfoResponseDto,
                    chatRoom.getId(),
                    chatRoom.getTopicA(),
                    chatRoom.getTopicB(),
                    chatRoom.getContent(),
                    chatRoom.getCategory(),
                    "time",
                    chatRoom.getCreatedAt()
            );
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    //게시글 전체 조회
    public List<MainPageBoardResponseDto> getMainPageBoard() {
        //board 전체를 가져옴
        List<Board> boardList = boardRepository.findAll();
        //리턴할 값의 리스트를 정의
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();


        for (Board boards : boardList) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(boards.getUser());
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(
                    userInfoResponseDto,
                    boards.getId(),
                    boards.getTopicA(),
                    boards.getTopicB(),
                    boards.getWinner(),
                    boards.getContent(),
                    boards.getCategory(),
                    boards.getCreatedAt(),
                    boards.getComments().size(),
                    boards.getWarnBoards().size()
            );
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

    //게시글 상세 조회
    public MainPageBoardDetailResponseDto getMainPageBoardDetail(Long boardId) {
        Board boards = boardRepository.findById(boardId).orElseThrow(
                () -> new NullPointerException("게시글이 없습니다")
        );
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(boards.getUser());
        MainPageBoardDetailResponseDto mainPageBoardDetailResponseDto = new MainPageBoardDetailResponseDto(
                userInfoResponseDto,
                boards.getId(),
                boards.getTopicA(),
                boards.getTopicB(),
                boards.getWinner(),
                boards.getContent(),
                boards.getCategory(),
                boards.getCreatedAt(),
                boards.getComments().size(),
                boards.getWarnBoards().size()
        );
        return mainPageBoardDetailResponseDto;
    }

    //게시글 검색
    public List<MainPageBoardResponseDto> getSearchWord(String keyword) {
//        List<Board> boardList = boardRepository.findSearch(keyword);
        List<Board> boardList = boardRepository.findAllByTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(keyword, keyword);
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

        for (Board boards : boardList) {
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(boards.getUser());
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(
                    userInfoResponseDto,
                    boards.getId(),
                    boards.getTopicA(),
                    boards.getTopicB(),
                    boards.getWinner(),
                    boards.getContent(),
                    boards.getCategory(),
                    boards.getCreatedAt(),
                    boards.getComments().size(),
                    boards.getWarnBoards().size()
            );
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

}
