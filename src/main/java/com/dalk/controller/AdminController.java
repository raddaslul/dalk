package com.dalk.controller;


import com.dalk.domain.User;
import com.dalk.dto.requestDto.GivePointRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {

    private final AdminService adminService;

    // ADMIN 권한 가진 유저만 API에 접근 가능
    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/boards")
    @ApiOperation(value = "블라인드 게시글 조회")
    public List<WarnBoardResponseDto> getAdminBoard(){
        return adminService.getAdminMainPageBoard();
    }

    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/boards/{boardId}")
    @ApiOperation(value = "블라인드 게시글 삭제")
    public Map<String, Object> deleteAdminBoard(@PathVariable Long boardId){
        return adminService.deleteAdminBoard(boardId);
    }
    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/comments")
    @ApiOperation(value = "신고 댓글 조회")
    public List<WarnCommentResponseDto> getAdminComment() {return adminService.getAdminComment();}

    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/comments/{commentId}")
    @ApiOperation(value = "신고 댓글 삭제")
    public Map<String, Object> deleteAdminComment(@PathVariable Long commentId) {
        return adminService.deleteAdminComment(commentId);
    }

    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/rooms")
    @ApiOperation(value = "신고 토론방 목록 조회")
    public List<WarnChatRoomResponseDto> getAdminMainPageAll() {
        return adminService.getAdminMainPageAll();
    }

    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/rooms/{roomId}")
    @ApiOperation(value = "신고 토론방 삭제")
    public Map<String, Object> deleteAdminChatRoom(@PathVariable Long roomId){
        return adminService.deleteAdminChatRoom(roomId);
    }

    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/users")
    @ApiOperation(value = "신고 유저 목록 조회")
    public List<WarnUserResponseDto> getUserList(){
        return adminService.getUserList();
    }

    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/users/{userId}")
    @ApiOperation(value = "신고 유저 삭제")
    public Map<String, Object> deleteUser(@PathVariable Long userId){
        return adminService.deleteUser(userId);
    }

    @Secured(User.Role.Authority.ADMIN)
    @PostMapping("/eventpoint")
    @ApiOperation(value = "유저에게 포인트지급")
    public Map<String, Object> givePoint(@RequestBody GivePointRequestDto givePointRequestDto){
        return adminService.givePoint(givePointRequestDto);
    }
}
