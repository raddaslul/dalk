package com.dalk.service;

import com.dalk.domain.User;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private  PasswordEncoder passwordEncoder;
    private  UserRepository userRepository;

    //회원가입
    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        String nickname = requestDto.getNickname();
        Optional<User> found1 = userRepository.findByNickname(nickname);
        if (found1.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 닉네임이 존재합니다.");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());//비번 인코딩

        User user = new User(username, password, nickname);
        userRepository.save(user);
    }

}

