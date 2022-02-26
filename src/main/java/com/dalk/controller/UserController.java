package com.dalk.controller;



import com.dalk.dto.requestDto.UserRequestDto;
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
    public UserInfoResponseDto signup(@RequestBody @Valid UserRequestDto requestDto,
                                                   HttpServletResponse response) {
        return userService.signup(requestDto, response);
    }

    // 로그인
    @PostMapping("/users/login")
    public UserInfoResponseDto login(@RequestBody @Valid UserRequestDto requestDto,
                                                  HttpServletResponse response) {
        return userService.login(requestDto, response);
    }
}