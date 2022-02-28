package com.dalk;

import com.dalk.domain.User;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialData implements ApplicationRunner {

    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        User Admin = new User(
                "admin",
                "admin",
                "admin",
                1000L,
                100,
                User.Role.ADMIN);

        userRepository.save(Admin);
    }
}
