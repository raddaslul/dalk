package com.dalk.controller;



import com.dalk.dto.requestDto.UserRequestDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 회원 로그인 페이지
    @GetMapping("/users/loginView")
    public String login() {
        return "login";
    }




    // 회원 가입 페이지
    @GetMapping("/users/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/users/signup")
    public ResponseEntity<String> registerUser( @RequestBody UserRequestDto requestDto) {
        System.out.println(requestDto);
        userService.registerUser(requestDto);
        return ResponseEntity.ok()              //200코드랑 body에 success 가 들어간다 errorcode status .....
                .body("회원가입 완료");
    }
    // 회원 관련 정보 받기
    //
    @PostMapping("/users/userinfo")
    @ResponseBody
    public UserInfoResponseDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        String nickname = userDetails.getUser().getNickname();
        return new UserInfoResponseDto(username, nickname);
    }

}