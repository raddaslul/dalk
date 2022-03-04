package com.dalk.controller;


import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


//블라인드 게시글 조회
    @GetMapping("/admin/boards")
    public List<MainPageBoardResponseDto> getAdminBoard(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return adminService.getAdminMainPageBoard(userDetails);
    }

//    블라인드 게시글 삭제
    @DeleteMapping("/admin/boards/{boardId}")
    public void deleteAdminBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        adminService.deleteAdminBoard(boardId,userDetails);
    }

//    토론방 리스트 전체 조회 - 관리자
    @GetMapping("/admin/rooms")
    public List<MainPageAllResponseDto> getAdminMainPageAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.getAdminMainPageAll(userDetails);
    }

//    토론방 삭제 - 관리자
//
//    @DeleteMapping("")
//    public void deleteAdminChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return adminService.deleteAdminChatRoom(userDetails);
//    }


//
//    유저 목록 조회
    @GetMapping("/admin/users")
    public List<UserInfoResponseDto> getUserList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return adminService.getUserList(userDetails);
    }

//    유저 삭제 - 관리자 권한
    @DeleteMapping("/admin/users/{userId}")
    public void deleteUser(@PathVariable Long userId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        adminService.deleteUser(userId,userDetails);
    }
}
