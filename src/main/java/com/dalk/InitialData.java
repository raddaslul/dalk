package com.dalk;

import com.dalk.domain.*;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnBoardRepository;
import com.dalk.repository.wl.WarnChatRoomRepository;
import com.dalk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialData implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final PointRepository pointRepository;
    private final LottoRepository lottoRepository;
    private final CategoryRepository categoryRepository;
    private final VoteRepository voteRepository;
    private final WarnBoardRepository warnBoardRepository;
    private final WarnChatRoomRepository warnChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        String encPassword = passwordEncoder.encode("adminPass");

        User user1 = new User(
                "user1",
                encPassword,
                "user1",
                50000000L,
                100,
                0,
                User.Role.ADMIN);
        userRepository.save(user1);
        List<Item> itemTests1 = userService.getItemTests(user1);
        user1.setItems(itemTests1);
        userRepository.save(user1);

        Point point1 = new Point("회원가입", 50000000L, 50000000L, user1);
        pointRepository.save(point1);
        Lotto lotto1 = new Lotto(0, user1);
        lottoRepository.save(lotto1);


        User user2 = new User( //유저추가
                "user2",
                encPassword,
                "user2",
                100000000L,
                100,
                15,
                User.Role.USER);
        userRepository.save(user2);
        List<Item> itemTests2 = userService.getItemTests(user2);
        user2.setItems(itemTests2);
        userRepository.save(user2);

        Point point2 = new Point("회원가입", 100000000L, 100000000L, user2);
        pointRepository.save(point2);
        Lotto lotto2 = new Lotto(0, user2);
        lottoRepository.save(lotto2);

        Long userId1 = user1.getId();
        for (int i = 0; i < 20; i++) {
            Board board = new Board("topicA", "topicB", "topicA", userId1);
            boardRepository.save(board);
            Vote vote = new Vote(board, 10L, 25000L, 17500F, 8L, 1200L, 42000F);
            voteRepository.save(vote);
            board.setVote(vote);
            boardRepository.save(board);
        }

        Board board1 = new Board( //게시글 추가
                "짜장",
                "짬뽕",
                "승자",
                userId1
        );
        boardRepository.save(board1);
        Vote vote1 = new Vote(board1, 10L, 25000L, 17500F, 10L, 1200L, 42000F);
        voteRepository.save(vote1);
        board1.setVote(vote1);
        boardRepository.save(board1);


        List<String> categoryList1 = new ArrayList<>();
        categoryList1.add("연애");
        categoryList1.add("오락");
        for (String stringCategory : categoryList1) {
            Category category = new Category(board1, stringCategory);
            categoryRepository.save(category);
        }

        Long userId2 = user2.getId();
        Board board2 = new Board(
                "짬뽕",
                "짜장",
                "승자2",
                userId2
        );
        boardRepository.save(board2);
        Vote vote2 = new Vote(board2, 10L, 17500L, 42000F, 7L, 20000L, 32000F);
        voteRepository.save(vote2);
        board2.setVote(vote2);
        boardRepository.save(board2);

        List<String> categoryList2 = new ArrayList<>();
        categoryList2.add("도움");
        categoryList2.add("사랑");
        for (String stringCategory : categoryList2) {
            Category category = new Category(board2, stringCategory);
            categoryRepository.save(category);
        }


        Comment comment1 = new Comment(
                "안녕하세요1",
                board1,
                userId1
        );
        Comment comment2 = new Comment(
                "안녕하세요2",
                board1,
                userId2
        );
        commentRepository.save(comment1);
        commentRepository.save(comment2);


        User user3 = new User( //유저추가
                "user3",
                encPassword,
                "user3",
                500L,
                100,
                0,
                User.Role.USER);
        userRepository.save(user3);
        List<Item> itemTests3 = userService.getItemTests(user3);
        user3.setItems(itemTests3);
        userRepository.save(user3);

        Point point3 = new Point("회원가입", 500L, 500L, user3);
        pointRepository.save(point3);
        Lotto lotto3 = new Lotto(0, user3);
        lottoRepository.save(lotto3);


        User user4 = new User( //유저추가
                "user4",
                encPassword,
                "user4",
                500L,
                100,
                0,
                User.Role.USER);
        userRepository.save(user4);
        List<Item> itemTests4 = userService.getItemTests(user4);
        user4.setItems(itemTests4);
        userRepository.save(user4);

        Point point4 = new Point("회원가입", 500L, 500L, user4);
        pointRepository.save(point4);
        Lotto lotto4 = new Lotto(0, user4);
        lottoRepository.save(lotto4);


        User user5 = new User( //유저추가
                "user5",
                encPassword,
                "user5",
                500L,
                100,
                16,
                User.Role.USER);
        userRepository.save(user5);
        List<Item> itemTests5 = userService.getItemTests(user5);
        user5.setItems(itemTests5);
        userRepository.save(user5);

        Point point5 = new Point("회원가입", 500L, 500L, user5);
        pointRepository.save(point5);
        Lotto lotto5 = new Lotto(0, user5);
        lottoRepository.save(lotto5);


        User user6 = new User( //유저추가
                "user6",
                encPassword,
                "user6",
                500L,
                100,
                10,
                User.Role.USER);
        userRepository.save(user6);
        List<Item> itemTests6 = userService.getItemTests(user6);
        user6.setItems(itemTests6);
        userRepository.save(user6);

        Point point6 = new Point("회원가입", 500L, 500L, user6);
        pointRepository.save(point6);
        Lotto lotto6 = new Lotto(0, user6);
        lottoRepository.save(lotto6);


        User user7 = new User( //유저추가
                "user7",
                encPassword,
                "user7",
                500L,
                100,
                5,
                User.Role.USER);
        userRepository.save(user7);
        List<Item> itemTests7 = userService.getItemTests(user7);
        user7.setItems(itemTests7);
        userRepository.save(user7);

        Point point7 = new Point("회원가입", 500L, 500L, user7);
        pointRepository.save(point7);
        Lotto lotto7 = new Lotto(0, user7);
        lottoRepository.save(lotto7);

        WarnBoard warnBoard1 = new WarnBoard(true, board1, user1);
        warnBoardRepository.save(warnBoard1);
        WarnBoard warnBoard2 = new WarnBoard(true, board1, user2);
        warnBoardRepository.save(warnBoard2);
        WarnBoard warnBoard3 = new WarnBoard(true, board1, user3);
        warnBoardRepository.save(warnBoard3);
        WarnBoard warnBoard4 = new WarnBoard(true, board1, user4);
        warnBoardRepository.save(warnBoard4);
        WarnBoard warnBoard5 = new WarnBoard(true, board1, user5);
        warnBoardRepository.save(warnBoard5);
        WarnBoard warnBoard6 = new WarnBoard(true, board2, user1);
        warnBoardRepository.save(warnBoard6);
        WarnBoard warnBoard7 = new WarnBoard(true, board2, user2);
        warnBoardRepository.save(warnBoard7);
        WarnBoard warnBoard8 = new WarnBoard(true, board2, user3);
        warnBoardRepository.save(warnBoard8);
        WarnBoard warnBoard9 = new WarnBoard(true, board2, user4);
        warnBoardRepository.save(warnBoard9);
        WarnBoard warnBoard10 = new WarnBoard(true, board2, user5);
        warnBoardRepository.save(warnBoard10);


//        List<Category> categorys1 = null;
//        ChatRoom chatRoom1 = new ChatRoom(
//                "topicA",
//                "topicB",
//                categorys1,
//                false,
//                1L,
//                true
//        );
//        chatRoomRepository.save(chatRoom1);
//
//        Category category1 = new Category(
//                chatRoom1,
//                "음식"
//        );
//        categoryRepository.save(category1);
//
//        Category category2 = new Category(
//                chatRoom1,
//                "유머"
//        );
//        categoryRepository.save(category2);
//
//        categorys1 = new ArrayList<>();
//        categorys1.add(category1);
//        categorys1.add(category2);
//
//
//        WarnChatRoom warnChatRoom1 = new WarnChatRoom(
//                true,
//                chatRoom1,
//                user2
//        );
//        WarnChatRoom warnChatRoom2 = new WarnChatRoom(
//                true,
//                chatRoom1,
//                user3
//        );
//        WarnChatRoom warnChatRoom3 = new WarnChatRoom(
//                true,
//                chatRoom1,
//                user4
//        );
//        warnChatRoomRepository.save(warnChatRoom1);
//        warnChatRoomRepository.save(warnChatRoom2);
//        warnChatRoomRepository.save(warnChatRoom3);

        for (int i = 0; i < 20; i++) {
            Board board = new Board(
                    "topicA",
                    "topicB",
                    "topicA",
                    userId1);
            boardRepository.save(board);
            Vote vote = new Vote(
                    board,
                    10L,
                    25000L,
                    17500F,
                    8L,
                    1200L, 42000F);
            voteRepository.save(vote);
            board.setVote(vote);
            boardRepository.save(board);
        }


        for (int i = 0; i < 20; i++) {
            List<Category> categorys = null;
            ChatRoom chatRoom = new ChatRoom(
                    "topicA",
                "topicB",
                categorys,
                false,
                1L,
                true
            );
            chatRoomRepository.save(chatRoom);
            Category category = new Category(
                chatRoom,
                "음식"
        );
        categoryRepository.save(category);
            categorys = new ArrayList<>();
            categorys.add(category);
        }




    }
}


