package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

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
    //포인트 내역 조회
//    @GetMapping("/mypage/points")
//    public ResponseEntity<PointsResponseDto> getPoint(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        User user = userDetails.getUser();
//        return ResponseEntity.ok().body(myPageService.getPoint(user));
//    }



    //계정꾸미기
//    @PostMapping("/mypage/item")
//    public ItemResponseDto buyitem(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return MyPageService.
//    }

}
