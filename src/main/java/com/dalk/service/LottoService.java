package com.dalk.service;

import com.dalk.DalkApplication;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.LottoResponseDto;
import com.dalk.exception.ex.LackPointException;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LottoService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public LottoResponseDto getLotto(User user) throws NoSuchAlgorithmException {
        Long lottoPrice = 200L;
        if(user.getTotalPoint()>=lottoPrice) {
            user.setTotalPoint(user.getTotalPoint()-lottoPrice);
            userRepository.save(user);
            Point point = new Point("로또 참여", -lottoPrice, user.getTotalPoint(), user);
            pointRepository.save(point);
            String content = "";
            Random random = SecureRandom.getInstanceStrong();
            int num = random.nextInt(10000);
            if (1 <= num && num < 30) {//0.3프로
                content = "경 * 축 1등 당첨!!😄😄😄 세상에 이런일이.... 운이 엄청 좋으시군요...!! ";
                lottoPoint1(user);
            }else if(30<= num && num < 130){ //1프로
                content = "경 * 축 2등 당첨!!😁😁😁 2등도 잘한거야!!!!";
                lottoPoint2(user);
            } else if (130 <= num && num < 630) { // 5프로
                content = "경 * 축 3등 당첨!!😃😃😃 3등도 엄청난거지 ^^ 메달도 3등까지라구~";
                lottoPoint3(user);
            } else if (630 <= num && num < 1630) { //10프로
                content = "축! 4등 당첨!!😎😎😎";
                lottoPoint4(user);
            } else if (1630 <= num && num < 3630) { //20프로
                content = "5등 당첨!!😙😙😙";
                lottoPoint5(user);
            } else { //63.7프로
                content = "아쉽게도 꽝입니다 😥😥😥😥 한번만 더 뽑으면 1등 당첨될수도...?";
                lottoPoint6(user);
            }
            return new LottoResponseDto(content);
        }else throw new LackPointException("보유한 포인트가 부족합니다");
    }

    public void lottoPoint1(User user) {
        Long point1 = 1000000L;
        user.setTotalPoint(user.getTotalPoint()+point1);
        userRepository.save(user);
        Point point = new Point("1등 당첨", point1, user.getTotalPoint(), user);
        pointRepository.save(point);
    }
    public void lottoPoint2(User user) {
        Long point1 = 100000L;
        user.setTotalPoint(user.getTotalPoint()+point1);
        userRepository.save(user);
        Point point = new Point("2등 당첨", point1, user.getTotalPoint(), user);
        pointRepository.save(point);

    }
    public void lottoPoint3(User user) {
        Long point1 = 10000L;
        user.setTotalPoint(user.getTotalPoint()+point1);
        userRepository.save(user);
        Point point = new Point("3등 당첨", point1, user.getTotalPoint(), user);
        pointRepository.save(point);

    }
    public void lottoPoint4(User user) {
        Long point1 = 1000L;
        user.setTotalPoint(user.getTotalPoint()+point1);
        userRepository.save(user);
        Point point = new Point("4등 당첨", point1, user.getTotalPoint(), user);
        pointRepository.save(point);

    }
    public void lottoPoint5(User user) {
        Long point1 = 500L;
        user.setTotalPoint(user.getTotalPoint()+point1);
        userRepository.save(user);
        Point point = new Point("5등 당첨", point1, user.getTotalPoint(), user);
        pointRepository.save(point);

    }
    public void lottoPoint6(User user) {

    }
}
