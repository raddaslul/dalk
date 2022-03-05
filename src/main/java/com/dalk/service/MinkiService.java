package com.dalk.service;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinkiService {
    private static PointRepository pointRepository;

    //항해99 5기 B반 김민기님이 해줌
    @Autowired
    public MinkiService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public static UserInfoResponseDto userInfo(User user) {
        Point point = pointRepository.findTopByUserId(user.getId());
        user.setPoint(point.getToTalPoint());
        return new UserInfoResponseDto(user);
    }

}
