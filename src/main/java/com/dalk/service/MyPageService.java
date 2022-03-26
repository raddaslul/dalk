package com.dalk.service;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.PointResponseDto;
import com.dalk.dto.responseDto.RankResponseDto;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
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
        userRepository.deleteById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
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
        //나중에 지우기 책임자 현지훈
        StaticService.saveRank();

        List<User> rankList = userRepository.findTop99ByOrderByExDesc();
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
