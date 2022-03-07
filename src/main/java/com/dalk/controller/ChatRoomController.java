package com.dalk.controller;

import com.dalk.dto.requestDto.ChatRoomRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.ChatRoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/rooms")
    @ApiOperation(value = "토론방 생성")
    public HashMap<String, Object> createChatRoom(@RequestBody ChatRoomRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long roomId = chatRoomService.createChatRoom(userDetails, requestDto);
        HashMap<String, Object> result = new HashMap<>();
        result.put("roomId", roomId);
        return result;
    }

    @GetMapping("/api/main/rooms")
    @ApiOperation(value = "토론방 리스트 조회 top6")
    public List<MainPageAllResponseDto> getMainPageTop6() {
        return chatRoomService.getMainPageTop6();
    }

    @GetMapping("/api/rooms")
    @ApiOperation(value = "토론방 리스트 전체 조회")
    public List<MainPageAllResponseDto> getMainPageAll() {
        return chatRoomService.getMainPageAll();
    }

    @GetMapping("/rooms/{roomId}")
    @ApiOperation(value = "채팅방 클릭시 방 넘어가는 기능")
    public MainPageAllResponseDto getMainPageOne(@PathVariable Long roomId) {
        return chatRoomService.getMainPageOne(roomId);
    }

    @GetMapping("/api/main/{category}")
    @ApiOperation(value = "카테고리 태그 검색")
    public List<MainPageAllResponseDto> getSerarchCategory(@PathVariable String category) {
        return chatRoomService.getSearchCategory(category);
    }

    //    @GetMapping("/api/main/{category}")
//    @ApiOperation(value = "카테고리 클릭시 검색") //카테고리 클릭 시 넘어가는 것
//    public List<MainPageAllResponseDto> getSerarchCategory(@PathVariable String category) {
//        return mainPageService.getSearchCategory2(category);
//    }
}
