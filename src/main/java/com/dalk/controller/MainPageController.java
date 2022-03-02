package com.dalk.controller;

import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardDetailResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageTop6ResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.MainPageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;

    @PostMapping("/rooms")
    @ApiOperation(value = "토론방 생성")
    public HashMap<String, Object> createChatRoom(UserDetailsImpl userDetails) {
        mainPageService.createChatRoom(userDetails);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    @GetMapping("/api/main/rooms")
    @ApiOperation(value = "토론방 리스트 조회 top6")
    public MainPageTop6ResponseDto getMainPageTop6() {
        return mainPageService.getMainPageTop6();
    }

    @GetMapping("/api/rooms")
    @ApiOperation(value = "토론방 리스트 전체 조회")
    public MainPageAllResponseDto getMainPageAll() {
        return mainPageService.getMainPageAll();
    }

    @GetMapping("/api/boards")
    @ApiOperation(value = "게시글 전체 조회")
    public List<MainPageBoardResponseDto> getMainPageBoard() {
        return mainPageService.getMainPageBoard();
    }

    @GetMapping("/api/boards/{boardId}")
    @ApiOperation(value = "게시글 상세 조회")
    public MainPageBoardDetailResponseDto getMainPageBoardDetail(@PathVariable Long boardId) {
        return mainPageService.getMainPageBoardDetail(boardId);
    }

    @GetMapping("/api/keywords/{keyword}")
    @ApiOperation(value = "게시글 검색")
    public List<MainPageBoardResponseDto> getSearchWord(@PathVariable String keyword) {
        return mainPageService.getSearchWord(keyword);
    }
}