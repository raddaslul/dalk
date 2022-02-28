package com.dalk.service;

import com.dalk.config.auth.UserDetailsImpl;
import com.dalk.domain.Board;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardDetailResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageTop6ResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MainPageService {

    private final BoardRepository boardRepository;

    //채팅방 생성
    public void createChatRoom(UserDetailsImpl userDetails) {
    }

    //토론방리스트 탑6 조회
    public MainPageTop6ResponseDto getMainPageTop6() {
        return new MainPageTop6ResponseDto();
    }

    //토론방리스트 전체조회
    public MainPageAllResponseDto getMainPageAll() {
        return new MainPageAllResponseDto();
    }

    //게시글 전체 조회
    public List<MainPageBoardResponseDto> getMainPageBoard() {
        //board 전체를 가져옴
        List<Board> boardList = boardRepository.findAll();
        //리턴할 값의 리스트를 정의
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

        for (Board boards : boardList) {
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(
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
        MainPageBoardDetailResponseDto mainPageBoardDetailResponseDto = new MainPageBoardDetailResponseDto(
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
        List<Board> boardList = boardRepository.findBoard(keyword);
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();
        for (Board boards : boardList) {
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(
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
