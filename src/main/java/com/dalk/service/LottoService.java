package com.dalk.service;

import com.dalk.domain.Lotto;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.LottoResponseDto;
import com.dalk.exception.ex.LackPointException;
import com.dalk.repository.LottoRepository;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LottoService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final LottoRepository lottoRepository;

    public LottoResponseDto getLotto(User user) throws NoSuchAlgorithmException {
        Long lottoPrice = 200L;
        Lotto lotto = lottoRepository.findByUser(user);
        if(user.getTotalPoint()>=lottoPrice) {
            user.setTotalPoint(user.getTotalPoint()-lottoPrice);
            userRepository.save(user);
            Point point = new Point("로또 참여", -lottoPrice, user.getTotalPoint(), user);
            pointRepository.save(point);
            Random random = SecureRandom.getInstanceStrong();
            int num;
            if(lotto.getCount()>=5) {num = random.nextInt(3630);}
            else {num = random.nextInt(10000);}


            int content = 0;
            if (1 <= num && num < 30) {//0.3프로
                content = 1;
                lottoPoint1(user);
                lotto.setCount(0L);
                lottoRepository.save(lotto);
            }else if(30<= num && num < 130){ //1프로
                content = 2;
                lottoPoint2(user);
                lotto.setCount(0L);
                lottoRepository.save(lotto);
            } else if (130 <= num && num < 630) { // 5프로
                content = 3;
                lottoPoint3(user);
                lotto.setCount(0L);
                lottoRepository.save(lotto);
            } else if (630 <= num && num < 1630) { //10프로
                content = 4;
                lottoPoint4(user);
                lotto.setCount(0L);
                lottoRepository.save(lotto);
            } else if (1630 <= num && num < 3630) { //20프로
                content = 5;
                lottoPoint5(user);
                lotto.setCount(0L);
                lottoRepository.save(lotto);
            } else { //63.7프로
                    lottoPoint6(user);
                    lotto.setCount(lotto.getCount()+1);
                    lottoRepository.save(lotto);
            }
            return new LottoResponseDto(content, lotto.getCount());
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
