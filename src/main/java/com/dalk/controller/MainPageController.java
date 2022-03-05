package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.requestDto.MainPageRequest.CreateChatRoomRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.MainPageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainPageController {
    private final MainPageService mainPageService;

    @PostMapping("/rooms")
    @ApiOperation(value = "토론방 생성")
    public HashMap<String, Object> createChatRoom(@RequestBody CreateChatRoomRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long roomId = mainPageService.createChatRoom(userDetails, requestDto);
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomId", roomId);
        return result;
    }

    @GetMapping("/api/main/rooms")
    @ApiOperation(value = "토론방 리스트 조회 top6")
    public List<MainPageAllResponseDto> getMainPageTop6() {
        return mainPageService.getMainPageTop6();
    }

    @GetMapping("/api/rooms")
    @ApiOperation(value = "토론방 리스트 전체 조회")
    public List<MainPageAllResponseDto> getMainPageAll() {
        return mainPageService.getMainPageAll();
    }

    @GetMapping("/api/boards")
    @ApiOperation(value = "게시글 전체 조회")
    public List<MainPageBoardResponseDto> getMainPageBoard() {
        return mainPageService.getMainPageBoard();
    }

    @GetMapping("/api/boards/{boardId}")
    @ApiOperation(value = "게시글 상세 조회")
    public MainPageBoardResponseDto getMainPageBoardDetail(@PathVariable Long boardId) {
        return mainPageService.getMainPageBoardDetail(boardId);
    }

    @GetMapping("/api/keywords/{keyword}")
    @ApiOperation(value = "게시글 검색")
    public List<MainPageBoardResponseDto> getSearchWord(@PathVariable String keyword) {
        return mainPageService.getSearchWord(keyword);
    }

    @GetMapping("/api/main/{category}")
    @ApiOperation(value = "카테고리 클릭시 검색") //카테고리 클릭 시 넘어가는 것
    public List<MainPageAllResponseDto> getSerarchCategory(@PathVariable String category) {
        return mainPageService.getSearchCategory(category);
    }

//    @GetMapping("/rooms/{roomId}")
//    @ApiOperation(value = "채팅방 클릭시 방 넘어가는 기능")
//    public MainPageAllResponseDto getMainPageOne(@PathVariable Long roomId) {
//        return mainPageService.getMainPageOne(roomId);
//    }
}
