package com.dalk.service;

import com.dalk.domain.User;
import com.dalk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;


    @Transactional
    public String deleteUser(User user) {
        Long userId = user.getId();
        userRepository.deleteById(userId);
        return "회원탈퇴 되었습니다.";
    }

//    public ResponseEntity<PointsResponseDto> getPoint(User user) {
//
//
//    }
}
