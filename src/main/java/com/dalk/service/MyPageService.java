package com.dalk.service;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.PointResponseDto;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PointRepository pointRepository;


    @Transactional
    public String deleteUser(User user) {
        Long userId = user.getId();
        userRepository.deleteById(userId);
        return "회원탈퇴 되었습니다.";
    }

    public List<PointResponseDto> getPoint(Long userId) {
        Optional<User> user = userRepository.findByUsername(String.valueOf(userId));

        List<Point> pointList = pointRepository.findById(user);

        List<PointResponseDto> pointResponseDtoList =new ArrayList<>();

        for (Point point : pointList ){
            PointResponseDto pointResponseDto = pointResponse(point);
            pointResponseDtoList.add(pointResponseDto);
        }
        return pointResponseDtoList;
    }

    private PointResponseDto pointResponse(Point point) {

        User user = point.getUser();
        point.getChangePoint();
        point.getResultPoint();
        point.getContent();

        return new PointResponseDto(point);
    }

//    public ResponseEntity<PointsResponseDto> getPoint(User user) {
//
//
//    }
}
