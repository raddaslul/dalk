package com.dalk.service;

import com.dalk.DalkApplication;
import com.dalk.domain.Lotto;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.LottoResponseDto;
import com.dalk.exception.ex.LackPointException;
import com.dalk.repository.LottoRepository;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LottoService {

    @NoArgsConstructor
    @Getter
    public enum LottoType {
        ONE("1등 당첨", 1000000L, 1),
        TWO("2등 당첨", 100000L, 2),
        THREE("3등 당첨", 10000L, 3),
        FOUR("4등 당첨", 1000L, 4),
        FIVE("5등 당첨", 100L, 5),
        SIX("꽝", 0L, 6);

        private String content;
        private Long point;
        private Integer rank;

        LottoType(String content, Long point, Integer rank) {
            this.content = content;
            this.point = point;
            this.rank = rank;
        }
    }

    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final LottoRepository lottoRepository;

    public LottoResponseDto getLotto(User user) throws NoSuchAlgorithmException {
        Long lottoPrice = 200L;
        Lotto lotto = lottoRepository.findByUser(user);
        if (user.getTotalPoint() < lottoPrice) {
            throw new LackPointException("보유한 포인트가 부족합니다");
        }
        user.setTotalPoint(user.getTotalPoint() - lottoPrice);
        userRepository.save(user);

        Point point = new Point("로또 참여", -lottoPrice, user.getTotalPoint(), user);
        pointRepository.save(point);

        Random random = SecureRandom.getInstanceStrong();
        int num;
        if (lotto.getCount() >= 5) {
            num = random.nextInt(3630);
        } else {
            num = random.nextInt(10000);
        }

        if (num < 30) {//0.3프로
            return lotto(LottoType.ONE, user, lotto);
        }
        if (30 <= num && num < 130) { //1프로
            return lotto(LottoType.TWO, user, lotto);
        }
        if (130 <= num && num < 630) { // 5프로
            return lotto(LottoType.THREE, user, lotto);
        }
        if (630 <= num && num < 1630) { //10프로
            return lotto(LottoType.FOUR, user, lotto);
        }
        if (1630 <= num && num < 3630) { //20프로
            return lotto(LottoType.FIVE, user, lotto);
        }
        lotto.setCount(lotto.getCount() + 1);
        lottoRepository.save(lotto);
        return new LottoResponseDto(6, lotto.getCount());

    }

    public LottoResponseDto lotto(LottoType lottoType, User user, Lotto lotto) {
        user.setTotalPoint(user.getTotalPoint() + lottoType.getPoint());
        userRepository.save(user);

        Point point = new Point(lottoType.getContent(), lottoType.getPoint(), user.getTotalPoint(), user);
        pointRepository.save(point);

        lotto.setCount(0);

        lottoRepository.save(lotto);
        return new LottoResponseDto(lottoType.getRank(), lotto.getCount());
    }
}
