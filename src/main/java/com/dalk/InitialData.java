package com.dalk;

import com.dalk.domain.*;
import com.dalk.repository.*;
import com.dalk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialData implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PointRepository pointRepository;
    private final UserService userService;
    private final RankRepository rankRepository;

    @Override
    public void run(ApplicationArguments args) {
        String encPassword = passwordEncoder.encode("adminPass");

        User user1 = new User(
                "user1",
                encPassword,
                "user1",
                0L,
                0,
                0,
                User.Role.ADMIN);
        userRepository.save(user1);
        List<Item> itemTests1 = userService.getItemTests(user1);
        user1.setItems(itemTests1);
        userRepository.save(user1);

        Point point1 = new Point("회원가입", 0L, 0L, user1);
        pointRepository.save(point1);



        User user2 = new User( //유저추가
                "user2",
                encPassword,
                "user2",
                0L,
                0,
                0,
                User.Role.ADMIN);
        userRepository.save(user2);
        List<Item> itemTests2 = userService.getItemTests(user2);
        user2.setItems(itemTests2);
        userRepository.save(user2);

        Point point2 = new Point("회원가입", 0L, 0L, user2);
        pointRepository.save(point2);



        User user3 = new User( //유저추가
                "user3",
                encPassword,
                "user3",
                0L,
                0,
                0,
                User.Role.ADMIN);
        userRepository.save(user3);
        List<Item> itemTests3 = userService.getItemTests(user3);
        user3.setItems(itemTests3);
        userRepository.save(user3);

        Point point3 = new Point("회원가입", 0L, 0L, user3);
        pointRepository.save(point3);


        User user4 = new User( //유저추가
                "user4",
                encPassword,
                "user4",
                0L,
                0,
                0,
                User.Role.ADMIN);
        userRepository.save(user4);
        List<Item> itemTests4 = userService.getItemTests(user4);
        user4.setItems(itemTests4);
        userRepository.save(user4);

        Point point4 = new Point("회원가입", 0L, 0L, user4);
        pointRepository.save(point4);



        User user5 = new User( //유저추가
                "goodgoddog",
                encPassword,
                "goodgoddog",
                0L,
                0,
                0,
                User.Role.ADMIN);
        userRepository.save(user5);
        List<Item> itemTests5 = userService.getItemTests(user5);
        user5.setItems(itemTests5);
        userRepository.save(user5);

        Point point5 = new Point("회원가입", 0L, 0L, user5);
        pointRepository.save(point5);


        Ranking ranking1 = new Ranking(1L, user1);
        rankRepository.save(ranking1);
        Ranking ranking2 = new Ranking(2L, user2);
        rankRepository.save(ranking2);
        Ranking ranking3 = new Ranking(3L, user3);
        rankRepository.save(ranking3);
    }
}