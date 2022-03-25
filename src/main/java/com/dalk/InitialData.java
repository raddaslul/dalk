//package com.dalk;
//
//import com.dalk.domain.*;
//import com.dalk.domain.vote.Vote;
//import com.dalk.domain.wl.WarnBoard;
//import com.dalk.domain.wl.WarnChatRoom;
//import com.dalk.repository.*;
//import com.dalk.repository.wl.WarnBoardRepository;
//import com.dalk.repository.wl.WarnChatRoomRepository;
//import com.dalk.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class InitialData implements ApplicationRunner {
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//    private final BoardRepository boardRepository;
//    private final CommentRepository commentRepository;
//    private final PointRepository pointRepository;
//    private final LottoRepository lottoRepository;
//    private final CategoryRepository categoryRepository;
//    private final VoteRepository voteRepository;
//    private final WarnBoardRepository warnBoardRepository;
//    private final WarnChatRoomRepository warnChatRoomRepository;
//    private final ChatRoomRepository chatRoomRepository;
//    private final UserService userService;
//    private final RankRepository rankRepository;
//
//    @Override
//    public void run(ApplicationArguments args) {
//        String encPassword = passwordEncoder.encode("adminPass");
//
//        User user1 = new User(
//                "user1",
//                encPassword,
//                "user1",
//                50000000L,
//                300,
//                0,
//                User.Role.ADMIN);
//        userRepository.save(user1);
//        List<Item> itemTests1 = userService.getItemTests(user1);
//        user1.setItems(itemTests1);
//        userRepository.save(user1);
//
//        Point point1 = new Point("회원가입", 50000000L, 50000000L, user1);
//        pointRepository.save(point1);
//        Lotto lotto1 = new Lotto(0, user1);
//        lottoRepository.save(lotto1);
//
//
//        User user2 = new User( //유저추가
//                "user2",
//                encPassword,
//                "user2",
//                100000000L,
//                200,
//                15,
//                User.Role.USER);
//        userRepository.save(user2);
//        List<Item> itemTests2 = userService.getItemTests(user2);
//        user2.setItems(itemTests2);
//        userRepository.save(user2);
//
//        Point point2 = new Point("회원가입", 100000000L, 100000000L, user2);
//        pointRepository.save(point2);
//        Lotto lotto2 = new Lotto(0, user2);
//        lottoRepository.save(lotto2);
//
//        User user3 = new User( //유저추가
//                "user3",
//                encPassword,
//                "user3",
//                100000000L,
//                100,
//                15,
//                User.Role.USER);
//        userRepository.save(user3);
//        List<Item> itemTests3 = userService.getItemTests(user3);
//        user3.setItems(itemTests3);
//        userRepository.save(user3);
//
//        Point point3 = new Point("회원가입", 100000000L, 100000000L, user3);
//        pointRepository.save(point3);
//        Lotto lotto3 = new Lotto(0, user3);
//        lottoRepository.save(lotto3);
//
//        Ranking ranking1 = new Ranking(1L, user1);
//        rankRepository.save(ranking1);
//        Ranking ranking2 = new Ranking(2L, user2);
//        rankRepository.save(ranking2);
//        Ranking ranking3 = new Ranking(3L, user3);
//        rankRepository.save(ranking3);
//    }
//}