package com.dalk.service;

import com.dalk.domain.User;
import com.dalk.dto.responseDto.PointsResponseDto;
import com.dalk.repository.UserRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.util.List;

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
