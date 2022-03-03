package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.dto.requestDto.CommentRequestDto;
import com.dalk.dto.responseDto.CommentResponseDto;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.exception.ex.BoardNotFoundException;
import com.dalk.repository.BoardRepository;
import com.dalk.repository.CommentRepository;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.PointRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final PointRepository pointRepository;
    private final ItemRepository itemRepository;

    //댓글 작성
    @Transactional
    public void createComment(Long boardId, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new BoardNotFoundException("해당 게시글은 없습니다."));
        Comment comment = new Comment(requestDto, user, board);
        commentRepository.save(comment);
    }

    //댓글 조회
    @Transactional
    public List<CommentResponseDto> getComment(Long boardId) {
        Board boards = boardRepository.findById(boardId).orElseThrow(
                ()-> new BoardNotFoundException("해당 게시글이 없습니다")
        );
        List<Comment> comments = commentRepository.findAllByBoard(boards);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : comments) {
            User user = comment.getUser();
            Point point = pointRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());

            List<ItemResponseDto> items = new ArrayList<>();
            for (ItemResponseDto itemResponseDto : items) {
                Item item = itemRepository.findByUser(user);
                String itemName = item.getItemName();
                Integer quantity = item.getQuantity();
                itemResponseDto = new ItemResponseDto(itemName, quantity);
                items.add(itemResponseDto);
            }
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(user, point, items);
            CommentResponseDto commentResponseDto = new CommentResponseDto(
                    userInfoResponseDto,
                    comment.getComment(),
                    comment.getLikeses().size(),
                    false
            );
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    //댓글 수정
    @Transactional
    public HashMap<String, Object> editComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comments = commentRepository.findById(commentId).orElseThrow(
                ()-> new NullPointerException("해당 댓글이 없습니다")
        );
        if (comments.getUser().getId().equals(userDetails.getUser().getId())) {
            comments.update(requestDto.getComment());
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "true");
            return result;
        } else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "사용자가 다릅니다");
            return result;
        }
    }

    //댓글 삭제
    @Transactional
    public HashMap<String, Object> deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comments = commentRepository.findById(commentId).orElseThrow(
                ()-> new NullPointerException("해당 댓글이 없습니다")
        );
        if (comments.getUser().getId().equals(userDetails.getUser().getId())) {
            commentRepository.deleteById(comments.getId());
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "true");
            return result;
        } else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "사용자가 다릅니다");
            return result;
        }
    }
}
