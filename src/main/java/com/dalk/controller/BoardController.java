package com.dalk.controller;

import com.dalk.dto.responseDto.MainPageResponse.DetailResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/boards")
    @ApiOperation(value = "게시글 전체 조회")
    public List<MainPageBoardResponseDto> getMainPageBoard(@RequestParam("page") int page,
                                                           @RequestParam("size") int size) {
        return boardService.getMainPageBoard(page, size);
    }

    @GetMapping("/api/boards/{boardId}")
    @ApiOperation(value = "게시글 상세 조회")
    public DetailResponseDto getMainPageBoardDetail(@PathVariable Long boardId) {
        return boardService.getMainPageBoardDetail(boardId);
    }

    @GetMapping("/api/keywords/{keyword}")
    @ApiOperation(value = "게시글 검색")
    public List<MainPageBoardResponseDto> getSearchWord(
            @PathVariable String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return boardService.getSearchWord(keyword, page, size);
    }

    @GetMapping("/api/boards/category/{category}")
    @ApiOperation(value = "카테고리탭 클릭시 그 카테고리 게시글만 나옴")
    public List<MainPageBoardResponseDto> getMainPageCreatedAt(@PathVariable String category,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size) {
        return boardService.getCategory(category, page, size);
    }

    @GetMapping("/warnings/boards/{boardId}")
    @ApiOperation(value = "게시글 신고하기")
    public Map<String, Object> WarnBoard(@PathVariable Long boardId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.warnBoard(boardId, userDetails);
    }
}
