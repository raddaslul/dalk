package com.dalk.service;


import com.dalk.config.jwt.JwtAuthenticationProvider;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.UserRequestDto;
import com.dalk.dto.responseDto.CMResponseDto;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public UserInfoResponseDto signup(UserRequestDto requestDto, HttpServletResponse response) {

        // 유효성 체크
//        if (!isPasswordMatched(username, rawPassword))
//            throw new PasswordContainsUsernameException("비밀번호에 아이디가 들어갈 수 없습니다.");
//
//        if (!isExistUsername(username))
//            throw new DuplicateUsernameException("이미 존재하는 아이디 입니다.");
//
//        if (!isExistNickname(nickname))
//            throw new DuplicationNicknameException("이미 존재하는 유저 네임 입니다.");
//
//        if (!isDuplicatePassword(rawPassword, pwCheck)) {
//            throw new PasswordNotCollectException();
//        }

        // 비밀번호 암호화
        String encPassword = passwordEncoder.encode(requestDto.getPassword());

        // User 객체 생성
        User user = User.builder()
                .username(requestDto.getUsername())
                .password(encPassword)
                .nickname(requestDto.getNickname())
                .build();

        // user 저장
        userRepository.save(requestDto.toEntity(user));

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .build();
        return login(userRequestDto, response);
    }


    public UserInfoResponseDto login(UserRequestDto requestDto, HttpServletResponse response) {
        User userEntity = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("가입되지 않은 이메일입니다.")
        );

//        if (!passwordEncoder.matches(requestDto.getPassword(), userEntity.getPassword()))
//            throw new PasswordNotCollectException();


        // 토큰 정보 생성
        String token = jwtAuthenticationProvider.createToken(userEntity.getUsername(), userEntity.getNickname());

        Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("가입되지 않은 이메일입니다.")
        );

        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .bold(false)
                .color("black")
                .build();

        return UserInfoResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .point(200L)
                .level(0)
                .role(User.Role.USER)
                .item(itemResponseDto)
                .build();
    }

//    private boolean isDuplicatePassword(String rawPassword, String pwCheck) {
//        return rawPassword.equals(pwCheck);
//    }
//
//    public boolean isExistUsername(String username) {
//        return !userRepository.findByUsername(username).isPresent();
//    }
//
//    public boolean isExistNickname(String nickname) {
//        return !userRepository.findByNickname(nickname).isPresent();
//    }
//
//    private boolean isPasswordMatched(String email, String rawPassword) {
//        String domain = email.split("@")[0];
//        return !rawPassword.contains(domain);
//    }

//    public ResponseEntity<CMResponseDto> idCheck(UserRequestDto requestDto) {
//        if(userRepository.findByUsername(requestDto.getUsername()).isPresent())
//            return ResponseEntity.ok(new CMResponseDto("false"));
//        return ResponseEntity.ok(new CMResponseDto("true"));
//    }
}

