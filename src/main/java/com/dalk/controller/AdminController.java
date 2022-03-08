package com.dalk.controller;


import com.dalk.domain.User;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.service.AdminService;
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

//블라인드 게시글 조회
    // ADMIN 권한 가진 유저만 API에 접근 가능
    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/boards")
    public List<MainPageBoardResponseDto> getAdminBoard(){
        return adminService.getAdminMainPageBoard();
    }

//    블라인드 게시글 삭제
    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/boards/{boardId}")
    public HashMap<String, Object> deleteAdminBoard(@PathVariable Long boardId){
        adminService.deleteAdminBoard(boardId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

//    토론방 리스트 전체 조회 - 관리자
    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/rooms")
    public List<MainPageAllResponseDto> getAdminMainPageAll() {
        return adminService.getAdminMainPageAll();
    }

//    토론방 삭제 - 관리자
    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/rooms/{roomId}")
    public HashMap<String, Object> deleteAdminChatRoom(@PathVariable Long roomId){
        adminService.deleteAdminChatRoom(roomId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

//    유저 목록 조회
    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/users")
    public List<UserInfoResponseDto> getUserList(){
        return adminService.getUserList();
    }

//    유저 삭제 - 관리자 권한
    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/users/{userId}")
    public HashMap<String, Object> deleteUser(@PathVariable Long userId){
        adminService.deleteUser(userId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }
}
