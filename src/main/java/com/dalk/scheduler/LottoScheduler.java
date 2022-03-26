package com.dalk.scheduler;

import com.dalk.domain.User;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LottoScheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 4 * * *") //매일 04시 00분 00초
    public void lottoTimer() {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            user.refreshCount();
            userRepository.save(user);
        }
    }
}
