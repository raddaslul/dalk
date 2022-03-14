package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.dto.responseDto.MainPageResponse.DetailResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnBoardResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnBoardRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final S3Repository s3Repository;

    // 토론방 종료 후 게시글 생성
    public void createBoard(ChatRoom chatRoom) {
        voteService.winVote(chatRoom.getId());
        Vote vote = voteRepository.findByChatRoom_Id(chatRoom.getId());
        vote.setChatRoom(null);
        voteRepository.save(vote);
        Board board = new Board(chatRoom);
        List<Category> categoryList = categoryRepository.findAllByChatRoom(chatRoom);
        if (chatRoom.getStatus()) {
            for (Category categorys : categoryList) {
                String stringCategory = categorys.getCategory();
                Category category = new Category(board, stringCategory);
                categoryRepository.save(category);
            }
            boardRepository.save(board);
            vote.setBoard(board);
            voteRepository.save(vote);
            board.setVote(vote);
            boardRepository.save(board);
        } else {
            voteRepository.delete(vote);
        }
        String deleteFileUrl = "image/" + chatRoom.getConvertedFileName();
        s3Repository.deleteFile(deleteFileUrl);
        chatRoomRepository.delete(chatRoom);
    }

    //게시글 전체 조회

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
    public DetailResponseDto getMainPageBoardDetail(Long boardId) {
        Board boards = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글이 없습니다")
        );
        List<Category> categoryList = categoryRepository.findCategoryByBoard_Id(boards.getId());
        User user = userRepository.findById(boards.getCreateUserId()).orElseThrow(
                () -> new LoginUserNotFoundException("유저 정보가 없습니다")
        );
        List<WarnBoard> warnBoardList = warnBoardRepository.findByBoardId(boards.getId());

        List<Long> warnUserList = new ArrayList<>();

        for (WarnBoard warnBoard : warnBoardList) {
            warnUserList.add(warnBoard.getUser().getId());
        }

        return new DetailResponseDto(boards, ItemService.categoryStringList(categoryList), user, warnBoardList.size(), warnUserList, boards.getVote());
    }

    //게시글 검색
    public List<MainPageBoardResponseDto> getSearchWord(String keyword) {
        List<Board> boardList = boardRepository.findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(keyword, keyword, keyword);

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

    //    게시글 신고하기
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
        return null;
    }
}
