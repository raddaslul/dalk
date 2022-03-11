package com.dalk;

import com.dalk.domain.*;
import com.dalk.domain.vote.Vote;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.repository.*;
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
    private final ItemRepository itemRepository;
    private final PointRepository pointRepository;
    private final LottoRepository lottoRepository;
    private final CategoryRepository categoryRepository;
    private final VoteRepository voteRepository;


    @Override
    public void run(ApplicationArguments args) {
        String encPassword = passwordEncoder.encode("adminPass");


        Item item1 = new Item(0, 0, 0,0,0);
        itemRepository.save(item1);
        User user1 = new User(
                "user1",
                encPassword,
                "user1",
                50000000L,
                1000000,
                User.Role.ADMIN,
                item1);
        userRepository.save(user1);
        item1.setUser(user1);
        itemRepository.save(item1);
        Point point1 = new Point("회원가입",50000000L,50000000L,user1);
        pointRepository.save(point1);
        Lotto lotto1 = new Lotto(0L, user1);
        lottoRepository.save(lotto1);

        Item item2 = new Item(0,0,0,0,0);
        itemRepository.save(item2);
        User user2 = new User( //유저추가
                "user2",
                encPassword,
                "user2",
                100000000L,
                100000,
                User.Role.USER,
                item2);
        userRepository.save(user2);
        item2.setUser(user2);
        itemRepository.save(item2);
        Point point2 = new Point("회원가입",100000000L,100000000L,user2);
        pointRepository.save(point2);
        Lotto lotto2 = new Lotto(0L, user2);
        lottoRepository.save(lotto2);

        Long userId1 = user1.getId();
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
        for(String stringCategory : categoryList1){
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
        for(String stringCategory : categoryList2){
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

        Item item3 = new Item(0,0,0,0,0);
        itemRepository.save(item3);
        User user3 = new User( //유저추가
                "user3",
                encPassword,
                "user3",
                500L,
                1000,
                User.Role.USER,
                item3);
        userRepository.save(user3);
        item3.setUser(user3);
        itemRepository.save(item3);
        Point point3 = new Point("회원가입",500L,500L,user3);
        pointRepository.save(point3);
        Lotto lotto3 = new Lotto(0L, user3);
        lottoRepository.save(lotto3);

        Item item4 = new Item(0,0,0,0,0);
        itemRepository.save(item4);
        User user4 = new User( //유저추가
                "user4",
                encPassword,
                "user4",
                500L,
                200,
                User.Role.USER,
                item4);
        userRepository.save(user4);
        item4.setUser(user4);
        itemRepository.save(item4);
        Point point4 = new Point("회원가입",500L,500L,user4);
        pointRepository.save(point4);
        Lotto lotto4 = new Lotto(0L, user4);
        lottoRepository.save(lotto4);

        Item item5 = new Item(0,0,0,0,0);
        itemRepository.save(item5);
        User user5 = new User( //유저추가
                "user5",
                encPassword,
                "user5",
                500L,
                10,
                User.Role.USER,
                item5);
        userRepository.save(user5);
        item5.setUser(user5);
        itemRepository.save(item5);
        Point point5 = new Point("회원가입",500L,500L,user5);
        pointRepository.save(point5);
        Lotto lotto5 = new Lotto(0L, user5);
        lottoRepository.save(lotto5);

        Item item6 = new Item(0,0,0,0,0);
        itemRepository.save(item6);
        User user6 = new User( //유저추가
                "user6",
                encPassword,
                "user6",
                500L,
                19,
                User.Role.USER,
                item6);
        userRepository.save(user6);
        item6.setUser(user6);
        itemRepository.save(item6);
        Point point6 = new Point("회원가입",500L,500L,user6);
        pointRepository.save(point6);
        Lotto lotto6 = new Lotto(0L, user6);
        lottoRepository.save(lotto6);

        Item item7 = new Item(0,0,0,0,0);
        itemRepository.save(item7);
        User user7 = new User( //유저추가
                "user7",
                encPassword,
                "user7",
                500L,
                107,
                User.Role.USER,
                item7);
        userRepository.save(user7);
        item7.setUser(user7);
        itemRepository.save(item7);
        Point point7 = new Point("회원가입",500L,500L,user7);
        pointRepository.save(point7);
        Lotto lotto7 = new Lotto(0L, user7);
        lottoRepository.save(lotto7);

    }
    }


