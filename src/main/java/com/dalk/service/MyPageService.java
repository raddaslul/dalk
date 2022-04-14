package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.dto.responseDto.PointResponseDto;
import com.dalk.dto.responseDto.RankResponseDto;
import com.dalk.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PointRepository pointRepository;


    @Transactional
    public Map<String, Object> deleteUser(User user) {
        Long userId = user.getId();

        return StaticService.deleteUserAllNull(userId);
    }

    @Transactional(readOnly = true)
    public List<PointResponseDto> getPoint(User user) {
        List<Point> pointList = pointRepository.findAllByUserOrderByCreatedAtDesc(user);
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
    @Transactional(readOnly = true)
    public List<RankResponseDto> getRank() {
        StaticService.saveRank();

        List<User> rankList = userRepository.findTop99ByOrderByExDescCreatedAtDesc();
        List<RankResponseDto> rankResponseDtoList =new ArrayList<>();
        Long rankNum;
        for(User user :rankList ){
            if(user.getRanking()==null){
                rankNum=null;
            }else {
                rankNum = user.getRanking().getId();
            }
            RankResponseDto rankResponseDto = new RankResponseDto(rankNum, user.getNickname(),user.getEx());
            rankResponseDtoList.add(rankResponseDto);
        }
        return rankResponseDtoList;
    }
}
