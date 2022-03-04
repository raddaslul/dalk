package com.dalk.service;

import com.dalk.domain.Item;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.repository.ItemRepository;
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
    private ItemRepository itemRepository;

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

        Item item = new Item(0,0,0);
        itemRepository.save(item);

//        Point point = new Point()

        User user = new User(username, password, nickname,0L,1, User.Role.USER, item);
        userRepository.save(user);


    }

    // 채팅방에서 유저 확인하기
    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("찾는 유저가 없습니다")
        );
        return user;
    }

}

