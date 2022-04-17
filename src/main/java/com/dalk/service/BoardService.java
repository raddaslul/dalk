package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.responseDto.MainPageResponse.DetailResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.VoteResultResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final WarnBoardRepository warnBoardRepository;
    private final VoteRepository voteRepository;
    private final VoteService voteService;

    // 토론방 종료 후 게시글 생성
    @Transactional
    public void createBoard(ChatRoom chatRoom) {
        Vote vote = voteService.winVote(chatRoom.getId());
        Board board = new Board(chatRoom);
        boardRepository.save(board);
        List<Category> categoryList = chatRoom.getCategorys();
        for (Category categorys : categoryList) {
            Category category = new Category(board, categorys.getCategory());
            categoryRepository.save(category);
        }

        vote.setChatRoom(null);
        voteRepository.save(vote);
        board.setVote(vote);
        defineWinner(board);
        boardRepository.save(board);
        isWarn(chatRoom, board);
        setChatMessageUserNull(chatRoom);
        chatRoomRepository.delete(chatRoom);
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public List<MainPageBoardResponseDto> getMainPageBoard(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return getMainPageBoardResponseDtoList(boardList);
    }

    //게시글 상세 조회
    @Transactional(readOnly = true)
    public DetailResponseDto getMainPageBoardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글이 없습니다")
        );
        List<Category> categoryList = board.getCategorys();
        Vote vote = board.getVote();
        List<WarnBoard> warnBoardList = board.getWarnBoards();
        List<Long> warnUserList = new ArrayList<>();
        for (WarnBoard warnBoard : warnBoardList) {
            warnUserList.add(warnBoard.getUser().getId());
        }
        if (vote.getTopicACnt() > vote.getTopicBCnt()) { //A가 이겼을 때
            return new DetailResponseDto(board, ItemService.categoryStringList(categoryList), warnBoardList.size(), warnUserList, whoWin(board, vote, true, true), whoWin(board, vote, false, false));
        } else if (vote.getTopicACnt() < vote.getTopicBCnt()) {
            return new DetailResponseDto(board, ItemService.categoryStringList(categoryList), warnBoardList.size(), warnUserList, whoWin(board, vote, true, false), whoWin(board, vote, false, true));
        } else {
            return new DetailResponseDto(board, ItemService.categoryStringList(categoryList), warnBoardList.size(), warnUserList, whoWin(board, vote, false, true), whoWin(board, vote, false, false));
        }
    }

    // 게시글 검색(카테고리, 제목)
    @Transactional(readOnly = true)
    public List<MainPageBoardResponseDto> getSearchWord(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardList = boardRepository.findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCaseOrderByCreatedAtDesc(keyword, keyword, keyword, pageable);

        return getMainPageBoardResponseDtoList(boardList);
    }

    // 카테고리로만 검색
    @Transactional
    public List<MainPageBoardResponseDto> getCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardList = boardRepository.findDistinctByCategorys_CategoryOrderByCreatedAtDesc(category, pageable);
        return getMainPageBoardResponseDtoList(boardList);
    }

    // 게시글 신고하기
    @Transactional
    public Map<String, Object> warnBoard(Long boardId, UserDetailsImpl userDetails) {
        Map<String, Object> result = new HashMap<>();
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글이 없습니다")
        );
        User user = userDetails.getUser();
        WarnBoard warnBoardCheck = warnBoardRepository.findByUserIdAndBoard(user.getId(), board).orElse(null);
        if (warnBoardCheck == null) {
            WarnBoard warnBoard = new WarnBoard(board, user);
            warnBoardRepository.save(warnBoard);
            result.put("result", true);
            return result;
        } else throw new WarnBoardDuplicateException("이미 신고한 게시글입니다.");
    }

    private List<MainPageBoardResponseDto> getMainPageBoardResponseDtoList(Page<Board> boardList) {
        List<MainPageBoardResponseDto> mainPageBoardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            List<Category> categoryList = board.getCategorys();
            List<WarnBoard> warnBoardList = board.getWarnBoards();
            MainPageBoardResponseDto mainPageBoardResponseDto = new MainPageBoardResponseDto(board, ItemService.categoryStringList(categoryList), warnBoardList.size(), null);
            mainPageBoardResponseDtoList.add(mainPageBoardResponseDto);
        }
        return mainPageBoardResponseDtoList;
    }

    private void defineWinner(Board board) {
        if (board.getVote().getTopicACnt() > board.getVote().getTopicBCnt()) {
            board.setWinner(board.getTopicA());
        } else if (board.getVote().getTopicACnt() < board.getVote().getTopicBCnt()) {
            board.setWinner(board.getTopicB());
        } else {
            board.setWinner("무승부");
        }
    }

    private void isWarn(ChatRoom chatRoom, Board board) {
        if (!chatRoom.getWarnChatRooms().isEmpty()) {
            List<WarnChatRoom> warnChatRoomList = chatRoom.getWarnChatRooms();
            for (WarnChatRoom warnChatRoom : warnChatRoomList) {
                WarnBoard warnBoard = new WarnBoard(warnChatRoom, board);
                warnBoardRepository.save(warnBoard);
            }
        }
    }

    private void setChatMessageUserNull(ChatRoom chatRoom) {
        List<ChatMessage> chatMessageList = chatRoom.getChatMessageList();
        for (ChatMessage chatMessage : chatMessageList) {
            chatMessage.setUser(null);
            chatMessageRepository.save(chatMessage);
        }
    }

    private VoteResultResponseDto whoWin(Board board, Vote vote, Boolean winner, Boolean AorB) {
        String rate;
        if (AorB) {
            if (winner) {
                rate = String.format("%.2f", ((vote.getTotalPointA() + vote.getTotalPointB()) / vote.getTotalPointA()));
            } else {
                rate = "0";
            }
            return new VoteResultResponseDto(board.getTopicA(), rate, String.format("%.0f", vote.getTotalPointA()), String.valueOf(vote.getTopicACnt()), String.valueOf(vote.getTopPointA()));
        } else {
            if (winner) {
                rate = String.format("%.2f", ((vote.getTotalPointA() + vote.getTotalPointB()) / vote.getTotalPointB()));
            } else {
                rate = "0";
            }
            return new VoteResultResponseDto(board.getTopicB(), rate, String.format("%.0f", vote.getTotalPointB()), String.valueOf(vote.getTopicBCnt()), String.valueOf(vote.getTopPointB()));
        }
    }
}