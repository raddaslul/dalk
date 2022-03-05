package com.dalk.service;

import com.dalk.domain.Board;
import com.dalk.domain.ChatRoom;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.MainPageRequest.CreateChatRoomRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.ChatRoomRepository;
import com.dalk.repository.PointRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MainPageService {

    private final BoardRepository boardRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final PointRepository pointRepository;

    //채팅방 생성
    public Long createChatRoom(UserDetailsImpl userDetails, CreateChatRoomRequestDto requestDto) {
        User user = userDetails.getUser();
        ChatRoom chatRoom = new ChatRoom(requestDto, user);
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

    //토론방리스트 탑6 조회
    public List<MainPageAllResponseDto> getMainPageTop6() {
        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findTop6ByOrderByCreatedAtDesc();
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom);
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    //토론방리스트 전체조회
    public List<MainPageAllResponseDto> getMainPageAll() {

        //board 전체를 가져옴
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByOrderByCreatedAtDesc();
        //리턴할 값의 리스트를 정의
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();

        UserInfoResponseDto userinfo = new UserInfoResponseDto();

        for (ChatRoom chatRoom : chatRoomList) {
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom);
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

        for (Board board : boardList) {
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(board);
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

    //게시글 상세 조회
    public MainPageBoardResponseDto getMainPageBoardDetail(Long boardId) {
        Board boards = boardRepository.findById(boardId).orElseThrow(
                () -> new NullPointerException("게시글이 없습니다")
        );
        return new MainPageBoardResponseDto(boards);
    }

    //게시글 검색
    public List<MainPageBoardResponseDto> getSearchWord(String keyword) {
//        List<Board> boardList = boardRepository.findSearch(keyword);
        List<Board> boardList = boardRepository.findAllByTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(keyword, keyword);
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

        for (Board boards : boardList) {
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(boards);
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

    //카테고리별 채팅방 조회
    public List<MainPageAllResponseDto> getSearchCategory(String category) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findByCategory(category);
        List<MainPageAllResponseDto> mainPageAllResponseDtoList = new ArrayList<>();

        for (ChatRoom chatRoom : chatRoomList) {
            MainPageAllResponseDto mainPageAllResponseDto = new MainPageAllResponseDto(chatRoom);
            mainPageAllResponseDtoList.add(mainPageAllResponseDto);
        }
        return mainPageAllResponseDtoList;
    }

    public MainPageAllResponseDto getMainPageOne(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                ()-> new NullPointerException("채팅방이 없습니다.")
        );
        return new MainPageAllResponseDto(chatRoom);
    }

}
