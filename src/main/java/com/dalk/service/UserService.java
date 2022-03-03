package com.dalk.service;


import com.dalk.domain.Item;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.exception.ex.DuplicateUsernameException;
import com.dalk.exception.ex.DuplicationNicknameException;
import com.dalk.exception.ex.PasswordNotEqualException;

import com.dalk.repository.ItemRepository;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private  PasswordEncoder passwordEncoder;
    private  UserRepository userRepository;
    private PointRepository pointRepository;

    //회원가입
    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new DuplicateUsernameException("중복된 사용자 ID 가 존재합니다.");
        }

        String nickname = requestDto.getNickname();
        Optional<User> found1 = userRepository.findByNickname(nickname);
        if (found1.isPresent()) {
            throw new DuplicationNicknameException("중복된 사용자 닉네임이 존재합니다.");
        }

        if(!requestDto.getPassword().equals(requestDto.getPasswordCheck())) {
            throw new PasswordNotEqualException("비밀번호가 일치하지 않습니다.");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());//비번 인코딩

        User user = new User(username, password, nickname);
        userRepository.save(user);

        Point point = new Point("signUp", 1000L, 1000L, user);
        pointRepository.save(point);
    }

    // 채팅방에서 유저 확인하기
    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("찾는 유저가 없습니다")
        );
        return user;
    }

}

