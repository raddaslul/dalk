package com.dalk.service;


import com.dalk.config.jwt.JwtAuthenticationProvider;
import com.dalk.domain.Item;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.LoginRequestDto;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.handler.ex.*;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public UserInfoResponseDto signup(SignupRequestDto signupRequestDto, HttpServletResponse response) {

        String username = signupRequestDto.getUsername(); // 아이디
        String rawPassword = signupRequestDto.getPassword();
        String pwCheck = signupRequestDto.getPasswordCheck();
        String nickname = signupRequestDto.getNickname();

        // 유효성 체크
        if (!isPasswordMatched(username, rawPassword))
            throw new PasswordContainsUsernameException("비밀번호에 아이디가 들어갈 수 없습니다.");

        if (!isExistUsername(username))
            throw new DuplicateUsernameException("이미 존재하는 아이디 입니다.");

        if (!isExistNickname(nickname))
            throw new DuplicationNicknameException("이미 존재하는 닉네임 입니다.");

        if (!isDuplicatePassword(rawPassword, pwCheck)) {
            throw new PasswordNotCollectException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encPassword = passwordEncoder.encode(rawPassword);

        // User 객체 생성
        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .password(encPassword)
                .nickname(signupRequestDto.getNickname())
                .point(200L)
                .level(0)
                .role(User.Role.USER)
                .build();

        // user 저장
        userRepository.save(user);

        Item item = Item.builder()
                .bold(false)
                .color("black")
                .user(user)
                .build();

        itemRepository.save(item);

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, rawPassword);
        return login(loginRequestDto, response);
    }

    public UserInfoResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        User userEntity = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("가입되지 않은 아이디입니다.")
        );

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), userEntity.getPassword()))
            throw new PasswordNotCollectException("비밀번호가 일치하지 않습니다.");


        // 토큰 정보 생성
        String token = jwtAuthenticationProvider.createToken(userEntity.getNickname(), userEntity.getUsername());

        Cookie cookie = new Cookie("Authorization", token);
        cookie.setPath("/"); // 모든 페이지에서 토큰 사용
        cookie.setHttpOnly(true); // 프론트에서 따로 못 꺼내 쓸 수 있게 만드는 코드
//        cookie.setSecure(true); // https 사용시 활성화
        response.addCookie(cookie); // 토큰을 헤더에 담아줌

        User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("가입되지 않은 아이디입니다.")
        );

        Item item = itemRepository.findByUser(user);
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        if(item != null) {
            itemResponseDto = new ItemResponseDto(item);
        }
        return new UserInfoResponseDto(user, itemResponseDto);
    }

    private boolean isDuplicatePassword(String rawPassword, String pwCheck) {
        return rawPassword.equals(pwCheck);
    }

    public boolean isExistUsername(String username) {
        return !userRepository.findByUsername(username).isPresent();
    }

    public boolean isExistNickname(String nickname) {
        return !userRepository.findByNickname(nickname).isPresent();
    }

    private boolean isPasswordMatched(String username, String rawPassword) {
        return !rawPassword.contains(username);
    }
}

