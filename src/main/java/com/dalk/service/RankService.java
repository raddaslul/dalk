package com.dalk.service;
import com.dalk.domain.Rank;
import com.dalk.domain.User;
import com.dalk.repository.RankRepository;
import com.dalk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class RankService {

    public static RankRepository rankRepository;
    public static UserRepository userRepository;

    @Autowired
    public RankService(RankRepository rankRepository,UserRepository userRepository) {
        RankService.rankRepository = rankRepository;
        RankService.userRepository = userRepository;
    }

    public static void saveRank() {
        List<User> top3rankList = userRepository.findTop3ByOrderByExDescCreatedAtDesc();

        Rank rank1 = new Rank(1L, top3rankList.get(0));
        rankRepository.save(rank1);
        Rank rank2 = new Rank(2L, top3rankList.get(1));
        rankRepository.save(rank2);
        Rank rank3 = new Rank(3L, top3rankList.get(2));
        rankRepository.save(rank3);
    }
}
