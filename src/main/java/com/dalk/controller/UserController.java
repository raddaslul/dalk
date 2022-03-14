package com.dalk.controller;



import com.dalk.domain.ItemType;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnUserResponseDto;
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
import javax.xml.ws.Response;
import java.util.HashMap;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ItemService itemService;

    @PostMapping("/users/signup")
    @ApiOperation(value = "회원가입")
    public String signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return "redirect:/users/login";
    }

    @GetMapping("/loginCheck")
    @ApiOperation(value = "로그인확인")
    public UserInfoResponseDto userInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return UserService.userInfo(userDetails.getUser());
    }

    @GetMapping("/mypage/{item}")
    @ApiOperation(value = "아이템 구매")
    public HashMap<String, Object> buyItem2(@PathVariable ItemType item, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        itemService.buyItem(item, user);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    @GetMapping("/chat/rooms/{item}")
    @ApiOperation(value = "아이템 사용")
    public HashMap<String, Object> userItem(@PathVariable ItemType item, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        itemService.useItem(item, user);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    @GetMapping("/warnings/{userId}")
    @ApiOperation(value = "유저 신고하기")
    public HashMap<String, Object> WarnUser
            (@PathVariable Long userId,
             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.WarnUser(userId, userDetails);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    //로그인 처리
    @GetMapping("/error")
    public ResponseEntity<ErrorResponse> error() {
        return new ResponseEntity<>(new ErrorResponse("400", "로그인 정보가 없습니다."), HttpStatus.BAD_REQUEST);
    }
}
