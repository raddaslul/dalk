package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.dto.responseDto.MainPageResponse.DetailResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.VoteResultResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnBoardResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.exception.ex.WarnBoardDuplicateException;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnBoardRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final WarnBoardRepository warnBoardRepository;
    private final VoteRepository voteRepository;
    private final VoteService voteService;

    // 토론방 종료 후 게시글 생성
    @Transactional
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
            vote.setBoard(board);
            voteRepository.save(vote);
            board.setVote(vote);
            if (board.getVote().getTopicACnt() > board.getVote().getTopicBCnt()) {
                board.setWinner(board.getTopicA());
            } else if (board.getVote().getTopicACnt() < board.getVote().getTopicBCnt()) {
                board.setWinner(board.getTopicB());
            }else{
                board.setWinner("무승부");
            }
            boardRepository.save(board);
        chatRoomRepository.delete(chatRoom);
        chatRoomUserRepository.deleteByChatRoom(chatRoom);
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public List<MainPageBoardResponseDto> getMainPageBoard(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);

        Page<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            List<Category> categoryList = categoryRepository.findCategoryByBoard_Id(board.getId());
            User user = userRepository.findById(board.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(board.getId());
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(board, ItemService.categoryStringList(categoryList), user, warnBoardList.size(), null);
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

    //게시글 상세 조회
    @Transactional(readOnly = true)
    public DetailResponseDto getMainPageBoardDetail(Long boardId) {
        Board boards = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글이 없습니다")
        );
        List<Category> categoryList = categoryRepository.findCategoryByBoard_Id(boards.getId());
        User user = userRepository.findById(boards.getCreateUserId()).orElseThrow(
                () -> new LoginUserNotFoundException("유저 정보가 없습니다")
        );
        Vote vote = boards.getVote();
        List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(boards.getId());
        List<Long> warnUserList = new ArrayList<>();
        for (WarnBoard warnBoard : warnBoardList) {
            warnUserList.add(warnBoard.getUser().getId());
        }
        if (vote.getTopicACnt() > vote.getTopicBCnt()) { //A가 이겼을 때
            return new DetailResponseDto(boards, ItemService.categoryStringList(categoryList), user, warnBoardList.size(), warnUserList ,whoWin(vote, true, true), whoWin(vote, false, false));
        } else if (vote.getTopicACnt() < vote.getTopicBCnt()) {
            return new DetailResponseDto(boards, ItemService.categoryStringList(categoryList), user, warnBoardList.size(), warnUserList ,whoWin(vote, true, false), whoWin(vote, false, true));
        } else {
            return new DetailResponseDto(boards, ItemService.categoryStringList(categoryList), user, warnBoardList.size(), warnUserList, whoWin(vote, false, true), whoWin(vote, false, false));
        }
    }

    public VoteResultResponseDto whoWin(Vote vote, Boolean winner, Boolean AorB) {
        String rate;
        if (AorB) {
            if (winner) { rate = String.format("%.2f", ((vote.getTotalPointA() + vote.getTotalPointB()) / vote.getTotalPointA()));}
            else {rate = "0"; }
            return new VoteResultResponseDto(vote.getBoard().getTopicA(),rate,String.format("%.0f", vote.getTotalPointA()),String.valueOf(vote.getTopicACnt()),String.valueOf(vote.getTopPointA()));
        } else {
            if (winner) { rate = String.format("%.2f", ((vote.getTotalPointA() + vote.getTotalPointB()) / vote.getTotalPointB()));}
            else {rate = "0"; }
            return new VoteResultResponseDto(vote.getBoard().getTopicB(),rate,String.format("%.0f",vote.getTotalPointB()),String.valueOf(vote.getTopicBCnt()),String.valueOf(vote.getTopPointB()));
        }
    }

    //게시글 검색
    @Transactional(readOnly = true)
    public List<MainPageBoardResponseDto> getSearchWord(String keyword,int page, int size) {

        Pageable pageable = PageRequest.of(page,size);
        Page<Board> boardList = boardRepository.findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCaseOrderByCreatedAtDesc(keyword, keyword, keyword,pageable);

        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

        for (Board boards : boardList) {
            List<Category> categoryList = categoryRepository.findCategoryByBoard_Id(boards.getId());
            User user = userRepository.findById(boards.getCreateUserId()).orElseThrow(
                    () -> new LoginUserNotFoundException("유저 정보가 없습니다")
            );
            List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(boards.getId());
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(boards, ItemService.categoryStringList(categoryList), user, warnBoardList.size(), null);
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

    // 게시글 신고하기
    @Transactional(readOnly = true)
    public WarnBoardResponseDto warnBoard(Long boardId, UserDetailsImpl userDetails) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글이 없습니다")
        );
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new LoginUserNotFoundException("유저 정보가 없습니다.")
        );
        WarnBoardResponseDto warnBoardResponseDto = new WarnBoardResponseDto();
        WarnBoard warnBoardCheck = warnBoardRepository.findByUserIdAndBoard(userDetails.getUser().getId(), board).orElse(null);
        if (warnBoardCheck == null) {
            WarnBoard warnBoard = new WarnBoard(true, board, user);
            warnBoardRepository.save(warnBoard);
            warnBoardResponseDto.setBoardId(warnBoard.getBoard().getId());
            warnBoardResponseDto.setWarn(warnBoard.getIsWarn());
            return warnBoardResponseDto;
        }
        else throw new WarnBoardDuplicateException("이미 신고한 게시글입니다.");
    }
}