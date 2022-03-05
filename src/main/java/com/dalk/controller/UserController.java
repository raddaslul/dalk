package com.dalk.controller;



import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.PointRepository;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.MinkiService;
import com.dalk.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PointRepository pointRepository;
    // 회원가입
    @PostMapping("/users/signup")
    @ApiOperation(value = "회원가입")
    public String signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return "redirect:/users/login";
    }

    //로그인 확인
    @GetMapping("/loginCheck")
    @ApiOperation(value = "로그인확인")
    public UserInfoResponseDto userInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        User user = userDetails.getUser();
//        Point point = pointRepository.findTopByUserId(user.getId());
//        user.setPoint(point.getToTalPoint());
//        return new UserInfoResponseDto(user);
        return MinkiService.userInfo(userDetails.getUser());
    }

//    @PostMapping("/mypage/{item}")
//    @ApiOperation(value = "아이템 구매")
//    public HashMap<String, Object> buyItem(@PathVariable String item, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        User user = userDetails.getUser();
//        userService.buyItem(item, user);
//
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("result", "true");
//        return result;
//    }
}
