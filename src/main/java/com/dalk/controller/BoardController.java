package com.dalk.controller;

import com.dalk.dto.responseDto.MainPageResponse.DetailResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnBoardResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/boards")
    @ApiOperation(value = "게시글 전체 조회")
    public List<MainPageBoardResponseDto> getMainPageBoard(
            @RequestParam("page") int page,
            @RequestParam("size") int size
//            @RequestParam("sortBy") String sortBy,
//            @RequestParam("isAsc") boolean isAsc
        ){

        return boardService.getMainPageBoard(page,size);
    }

    @GetMapping("/api/boards/{boardId}")
    @ApiOperation(value = "게시글 상세 조회")
    public DetailResponseDto getMainPageBoardDetail(@PathVariable Long boardId) {
        return boardService.getMainPageBoardDetail(boardId);
    }

    @GetMapping("/api/keywords/{keyword}")
    @ApiOperation(value = "게시글 검색")
    public List<MainPageBoardResponseDto> getSearchWord(@PathVariable String keyword) {
        return boardService.getSearchWord(keyword);
    }

    @GetMapping("/warnings/boards/{boardId}")
    @ApiOperation(value = "게시글 신고하기")
    public WarnBoardResponseDto WarnBoard
            (@PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        return boardService.warnBoard(boardId,userDetails);
    }
}
