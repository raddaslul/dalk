package com.dalk;

import com.dalk.domain.User;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialData implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        String encPassword = passwordEncoder.encode("adminPass");
        User admin = new User(
                "adminUser",
                encPassword,
                "adminNick",
                1000L,
                100,
                User.Role.ADMIN);

        userRepository.save(admin);
    }
}
