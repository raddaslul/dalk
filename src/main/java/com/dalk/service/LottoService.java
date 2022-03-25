package com.dalk.service;

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
        ONE("1등 당첨", 40000L, 1),
        TWO("2등 당첨", 20000L, 2),
        THREE("3등 당첨", 6000L, 3),
        FOUR("4등 당첨", 2000L, 4),
        FIVE("5등 당첨", 1000L, 5),
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
        Long lottoPrice = 500L;
        Lotto lotto = lottoRepository.findByUser(user);
//        if (user.getTotalPoint() < lottoPrice) {
//            throw new LackPointException("보유한 포인트가 부족합니다");
//        }
//        user.setTotalPoint(user.getTotalPoint() - lottoPrice);\
        user.totalPointSubtract(lottoPrice);
        userRepository.save(user);

        Point point = new Point("로또 참여", -lottoPrice, user);
        pointRepository.save(point);

        Random random = SecureRandom.getInstanceStrong();
        int num;
        if (lotto.getCount() >= 5) {
            num = random.nextInt(3630);
        } else {
            num = random.nextInt(10000);
        }

        if (num < 50) {// 0.5프로
            return lotto(LottoType.ONE, user, lotto);
        }
        if (50 <= num && num < 150) { // 1프로
            return lotto(LottoType.TWO, user, lotto);
        }
        if (150 <= num && num < 450) { // 3프로
            return lotto(LottoType.THREE, user, lotto);
        }
        if (450 <= num && num < 1450) { // 10프로 1000
            return lotto(LottoType.FOUR, user, lotto);
        }
        if (1450 <= num && num < 3450) { // 20프로 100
            return lotto(LottoType.FIVE, user, lotto);
        }
        lotto.addCount();
        lottoRepository.save(lotto);
        return new LottoResponseDto(6, lotto.getCount());

    }

    public LottoResponseDto lotto(LottoType lottoType, User user, Lotto lotto) {
//        user.setTotalPoint(user.getTotalPoint() + lottoType.getPoint());
        user.totalPointAdd(lottoType.getPoint());
        userRepository.save(user);

        Point point = new Point(lottoType.getContent(), lottoType.getPoint(), user);
        pointRepository.save(point);

        lotto.refreshCount();

        lottoRepository.save(lotto);
        return new LottoResponseDto(lottoType.getRank(), lotto.getCount());
    }
}
