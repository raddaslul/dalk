package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.responseDto.PointResponseDto;
import com.dalk.dto.responseDto.RankResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.MyPageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 유저 삭제하기(회원 탈퇴)
    @DeleteMapping("/signout")
    public HashMap<String,Object> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        myPageService.deleteUser(user);
        HashMap<String, Object> result = new HashMap<>();
            result.put("result", "true");
            return result;
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
