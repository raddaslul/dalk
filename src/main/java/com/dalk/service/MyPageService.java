package com.dalk.service;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.PointResponseDto;
import com.dalk.dto.responseDto.RankResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    @Transactional
    public void deleteUser(User user) {
        Long userId = user.getId();
        userRepository.deleteById(userId);
    }

    public List<PointResponseDto> getPoint(User user) {
        List<Point> pointList = pointRepository.findAllByUser(user);
        List<PointResponseDto> pointResponseDtoList =new ArrayList<>();

        for (Point point : pointList ){
            PointResponseDto pointResponseDto = pointResponse(point);
            pointResponseDtoList.add(pointResponseDto);
        }
        return pointResponseDtoList;
    }

    private PointResponseDto pointResponse(Point point) {
        return new PointResponseDto(point);
    }

    // 랭킹조회
    public List<RankResponseDto> getRank() {
        List<User> rankList = userRepository.findTop99ByOrderByExDesc();
        List<RankResponseDto> rankResponseDtoList =new ArrayList<>();

        for(User user1 :rankList ){
            RankResponseDto rankResponseDto = new RankResponseDto(user1.getNickname(),user1.getEx());
            rankResponseDtoList.add(rankResponseDto);
        }
        return rankResponseDtoList;
    }

}
