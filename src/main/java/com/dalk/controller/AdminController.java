package com.dalk.controller;


import com.dalk.domain.User;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {

    private final AdminService adminService;

    // ADMIN 권한 가진 유저만 API에 접근 가능
    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/boards")
    @ApiOperation(value = "블라인드 게시글 조회")
    public List<MainPageBoardResponseDto> getAdminBoard(){
        return adminService.getAdminMainPageBoard();
    }

    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/boards/{boardId}")
    @ApiOperation(value = "블라인드 게시글 삭제")
    public HashMap<String, Object> deleteAdminBoard(@PathVariable Long boardId){
        adminService.deleteAdminBoard(boardId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/rooms")
    @ApiOperation(value = "신고 토론방 목록 조회")
    public List<MainPageAllResponseDto> getAdminMainPageAll() {
        return adminService.getAdminMainPageAll();
    }

    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/rooms/{roomId}")
    @ApiOperation(value = "토론방 삭제")
    public HashMap<String, Object> deleteAdminChatRoom(@PathVariable Long roomId){
        adminService.deleteAdminChatRoom(roomId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }



    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/users")
    @ApiOperation(value = "신고 유저 목록 조회")
    public List<UserInfoResponseDto> getUserList(){
        return adminService.getUserList();
    }

    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/users/{userId}")
    @ApiOperation(value = "유저 삭제")
    public HashMap<String, Object> deleteUser(@PathVariable Long userId){
        adminService.deleteUser(userId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }
}
