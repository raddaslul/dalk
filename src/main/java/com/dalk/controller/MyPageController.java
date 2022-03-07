package com.dalk.controller;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.PointResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.MyPageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    // 유저 삭제하기(회원 탈퇴)
    @DeleteMapping("/signout")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(myPageService.deleteUser(user));
    }

    @GetMapping("/mypage/points")
    @ApiOperation(value = "포인트 내역 조회")
    public List<PointResponseDto> getPoint(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return myPageService.getPoint(user);
    }

}
