package com.dalk.controller;



import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.security.UserDetailsImpl;
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

    // 회원가입
    @PostMapping("/users/signup")
    @ApiOperation(value = "회원가입")
    public HashMap<String, Object> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    //로그인 확인
    @GetMapping("/loginCheck")
    @ApiOperation(value = "로그인확인")
    public UserInfoResponseDto userInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(userDetails.getUser());
        return userInfoResponseDto;
    }
}