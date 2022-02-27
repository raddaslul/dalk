package com.dalk.controller;



import com.dalk.dto.requestDto.LoginRequestDto;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/users/signup")
    @ApiOperation(value = "회원가입")
    public UserInfoResponseDto signup(@RequestBody @Valid SignupRequestDto signupRequestDto,
                                      HttpServletResponse response) {
        return userService.signup(signupRequestDto, response);
    }

    // 로그인
    @PostMapping("/users/login")
    @ApiOperation(value = "로그인")
    public UserInfoResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                     HttpServletResponse response) {
        return userService.login(loginRequestDto, response);
    }
}