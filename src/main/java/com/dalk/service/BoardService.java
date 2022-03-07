package com.dalk.service;

import com.dalk.domain.Board;
import com.dalk.domain.Category;
import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.CategoryRepository;
import com.dalk.repository.ChatRoomRepository;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // 토론방 종료 후 게시글 생성
    public void createBoard(ChatRoom chatRoom) {
//        Vote vote = voteRepository.findByRoomId(chatRoom.getId());
        Board board = new Board(chatRoom);
        boardRepository.save(board);
        List<Category> categoryList = categoryRepository.findAllByChatRoom(chatRoom);
        for (Category categorys : categoryList) {
            String stringCategory = categorys.getCategory();
            Category category = new Category(board, stringCategory);
            categoryRepository.save(category);
        }
    }

    //게시글 전체 조회
    public List<MainPageBoardResponseDto> getMainPageBoard() {
        List<Board> boardList = boardRepository.findAll();
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();
        for (Board board : boardList) {
            List<Category> categoryList = categoryRepository.findCategoryByBoard(board);
            User user = userRepository.findById(board.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(board, MinkiService.categoryStringList(categoryList),user);
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

    //게시글 상세 조회
    public MainPageBoardResponseDto getMainPageBoardDetail(Long boardId) {
        Board boards = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글이 없습니다")
        );
        List<Category> categoryList = categoryRepository.findCategoryByBoard(boards);
        User user = userRepository.findById(boards.getCreateUserId()).orElseThrow(
                () -> new LoginUserNotFoundException("유저 정보가 없습니다")
        );
        return new MainPageBoardResponseDto(boards, MinkiService.categoryStringList(categoryList), user);
    }

    //게시글 검색
    public List<MainPageBoardResponseDto> getSearchWord(String keyword) {
//        List<Board> boardList = boardRepository.findSearch(keyword);
        List<Category> categoryList = categoryRepository.findAllByBoard_TopicAContainingIgnoreCaseOrBoard_TopicBContainingIgnoreCaseOrCategory(keyword,keyword, keyword);
        List<Board> boardList = boardRepository.findAllByTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(keyword, keyword);
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

        for (Board boards : boardList) {
            User user = userRepository.findById(boards.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(boards, MinkiService.categoryStringList(categoryList), user);
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }
}
