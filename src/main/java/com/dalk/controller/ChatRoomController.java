package com.dalk.controller;

import com.dalk.dto.requestDto.ChatRoomRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnRoomResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.ChatRoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/rooms")
    @ApiOperation(value = "토론방 생성")
    public HashMap<String, Object> createChatRoom(
            @RequestPart(value = "image", required = false) MultipartFile multipartFile,
            @RequestPart("debate") ChatRoomRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        Long roomId = chatRoomService.createChatRoom(multipartFile, userDetails, requestDto);
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
    public List<MainPageAllResponseDto> getMainPageAll(   @RequestParam("page") int page,
                                                          @RequestParam("size") int size) {
        return chatRoomService.getMainPageAll(page,size);
    }

    @GetMapping("/rooms/{roomId}")
    @ApiOperation(value = "채팅방 클릭시 방 넘어가는 기능")
    public MainPageAllResponseDto getMainPageOne(@PathVariable Long roomId) {
        return chatRoomService.getMainPageOne(roomId);
    }

    @GetMapping("api/category/{category")
    @ApiOperation(value = "해당 카테고리 시간순 조회 (카테코리만 조회)")
    public List<MainPageAllResponseDto> getMainPageCreatedAt(@PathVariable String category,@RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        return chatRoomService.getMainPageCreatedAt(category, page, size);
    }

    @GetMapping("/api/main/{category}")
    @ApiOperation(value = "카테고리 태그 검색, 제목도 검색")
    public List<MainPageAllResponseDto> getSearchCategory(
            @PathVariable String category,
            @RequestParam("page") int page,
            @RequestParam("size") int size
            ) {
        return chatRoomService.getSearchCategory(category,page,size);
    }

    @GetMapping("/warnings/rooms/{roomId}")
    @ApiOperation(value = "토론방 신고하기")
    public WarnRoomResponseDto WarnChatRoom
            (@PathVariable Long roomId,
             @AuthenticationPrincipal UserDetailsImpl userDetails){

        return chatRoomService.WarnChatRoom(roomId,userDetails);
    }
}
