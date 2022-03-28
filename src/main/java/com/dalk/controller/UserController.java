package com.dalk.controller;

import com.dalk.domain.ItemType;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.NicknameCheckRequestDto;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.requestDto.UsernameCheckRequestDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.exception.ErrorResponse;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.UserService;
import com.dalk.service.ItemService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ItemService itemService;

    @PostMapping("/users/signup")
    @ApiOperation(value = "회원가입")
    public String signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);

        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return "redirect:/users/login";
    }

    @PostMapping("/users/signup/usernamecheck")
    @ApiOperation(value = "아이디 중복체크")
    public Map<String, Object> usernameCheck(@RequestBody UsernameCheckRequestDto requestDto) {
        return userService.usernameCheck(requestDto);
    }

    @PostMapping("/users/signup/nicknamecheck")
    @ApiOperation(value = "닉네임 중복체크")
    public Map<String, Object> nicknameCheck(@RequestBody NicknameCheckRequestDto requestDto) {
        return userService.nicknameCheck(requestDto);
    }

    @GetMapping("/loginCheck")
    @ApiOperation(value = "로그인확인")
    public UserInfoResponseDto userInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return UserService.userInfo(userDetails.getUser());
    }

    @GetMapping("/mypage/{item}")
    @ApiOperation(value = "아이템 구매")
    public Map<String, Object> buyItem2(@PathVariable ItemType item, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return itemService.buyItem(item, user);
    }

    @GetMapping("/chat/rooms/{item}")
    @ApiOperation(value = "아이템 사용")
    public Map<String, Object> userItem(@PathVariable ItemType item, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return itemService.useItem(item, user);
    }

    @GetMapping("/warnings/{userId}")
    @ApiOperation(value = "유저 신고하기")
    public Map<String, Object> WarnUser
            (@PathVariable Long userId,
             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.WarnUser(userId, userDetails);
    }

    //로그인 처리
    @GetMapping("/error")
    public ResponseEntity<ErrorResponse> error() {
        return new ResponseEntity<>(new ErrorResponse("400", "로그인 정보가 없습니다."), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/error/username")
    public ResponseEntity<ErrorResponse> usernameError() {
        return new ResponseEntity<>(new ErrorResponse("400", "username이 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/error/password")
    public ResponseEntity<ErrorResponse> passwordError() {
        return new ResponseEntity<>(new ErrorResponse("400", "password가 틀렸습니다."), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "healthy";
    }
}
