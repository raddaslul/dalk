package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnBoardResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnBoardRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final WarnBoardRepository warnBoardRepository;
    private final VoteRepository voteRepository;
    private final VoteService voteService;

    // 토론방 종료 후 게시글 생성
    public void createBoard(ChatRoom chatRoom) {
        voteService.winVote(chatRoom.getId());
        Vote vote = voteRepository.findByChatRoom_Id(chatRoom.getId());
        vote.setChatRoom(null);
        voteRepository.save(vote);
        Board board = new Board(chatRoom);
        List<Category> categoryList = categoryRepository.findAllByChatRoom(chatRoom);
        for (Category categorys : categoryList) {
            String stringCategory = categorys.getCategory();
            Category category = new Category(board, stringCategory);
            categoryRepository.save(category);
        }
        boardRepository.save(board);
        chatRoomRepository.delete(chatRoom);
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
            List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(board.getId());
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(board, MinkiService.categoryStringList(categoryList),user,warnBoardList.size());
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
        List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(boards.getId());

        return new MainPageBoardResponseDto(boards, MinkiService.categoryStringList(categoryList), user,warnBoardList.size());
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
            List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(boards.getId());
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(boards, MinkiService.categoryStringList(categoryList), user,warnBoardList.size());
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

//    게시글 신고하기
    public WarnBoardResponseDto warnBoard(Long boardId, UserDetailsImpl userDetails) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글이 없습니다")
        );
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()-> new LoginUserNotFoundException("유저 정보가 없습니다.")
        );

        WarnBoardResponseDto warnBoardResponseDto = new WarnBoardResponseDto();

        WarnBoard warnBoardCheck = warnBoardRepository.findByUserIdAndBoard(userDetails.getUser().getId(),board).orElse(null);

        if (warnBoardCheck==null){
            WarnBoard warnBoard =new WarnBoard(true,board,user);
            warnBoardRepository.save(warnBoard);
            warnBoardResponseDto.setBoardId(warnBoard.getBoard().getId());
            warnBoardResponseDto.setWarn(warnBoard.getIsWarn());
            return warnBoardResponseDto;
        }
        return null;




    }
}
