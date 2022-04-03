package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.responseDto.PointResponseDto;
import com.dalk.dto.responseDto.RankResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.MyPageService;
import com.dalk.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage")
    @ApiOperation(value = "유저 조회")
    public UserInfoResponseDto getMypage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new UserInfoResponseDto(userDetails.getUser());
    }

    @DeleteMapping("/signout")
    @ApiOperation(value = "회원 탈퇴")
    public Map<String,Object> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return myPageService.deleteUser(user);
    }

    @GetMapping("/mypage/points")
    @ApiOperation(value = "포인트 내역 조회")
    public List<PointResponseDto> getPoint(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return myPageService.getPoint(user);
    }

    @GetMapping("/api/ranks")
    @ApiOperation(value = "유저 랭킹조회")
    public List<RankResponseDto> updateRank(){
        return  myPageService.getRank();
    }
}