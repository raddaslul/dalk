package com.dalk;

import com.dalk.domain.*;
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
    private final CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) {
        String encPassword = passwordEncoder.encode("adminPass");


        Item item1 = new Item(0, 0, 0);
        itemRepository.save(item1);
        User user1 = new User(
                "user1",
                encPassword,
                "user1",
                50000000L,
                100,
                User.Role.ADMIN,
                item1);
        userRepository.save(user1);
        Point point1 = new Point("회원가입",50000000L,50000000L,user1);
        pointRepository.save(point1);


        Item item2 = new Item(0,0,0);
        itemRepository.save(item2);
        User user2 = new User( //유저추가
                "user2",
                encPassword,
                "user2",
                100000000L,
                100,
                User.Role.USER,
                item2);
        userRepository.save(user2);
        Point point2 = new Point("회원가입",100000000L,100000000L,user2);
        pointRepository.save(point2);

        Long userId1 = user1.getId();
        Board board1 = new Board( //게시글 추가
                "짜장",
                "짬뽕",
                "승자",
                userId1
        );
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

        Item item3 = new Item(0,0,0);
        itemRepository.save(item3);
        User user3 = new User( //유저추가
                "user3",
                encPassword,
                "user3",
                500L,
                100,
                User.Role.USER,
                item3);
        userRepository.save(user3);
        Point point3 = new Point("회원가입",500L,500L,user3);
        pointRepository.save(point3);

        Item item4 = new Item(0,0,0);
        itemRepository.save(item4);
        User user4 = new User( //유저추가
                "user4",
                encPassword,
                "user4",
                500L,
                100,
                User.Role.USER,
                item4);
        userRepository.save(user4);
        Point point4 = new Point("회원가입",500L,500L,user4);
        pointRepository.save(point4);

        Item item5 = new Item(0,0,0);
        itemRepository.save(item5);
        User user5 = new User( //유저추가
                "user5",
                encPassword,
                "user5",
                500L,
                100,
                User.Role.USER,
                item5);
        userRepository.save(user5);
        Point point5 = new Point("회원가입",500L,500L,user5);
        pointRepository.save(point5);

        Item item6 = new Item(0,0,0);
        itemRepository.save(item6);
        User user6 = new User( //유저추가
                "user6",
                encPassword,
                "user6",
                500L,
                100,
                User.Role.USER,
                item6);
        userRepository.save(user6);
        Point point6 = new Point("회원가입",500L,500L,user6);
        pointRepository.save(point6);

        Item item7 = new Item(0,0,0);
        itemRepository.save(item7);
        User user7 = new User( //유저추가
                "user7",
                encPassword,
                "user7",
                500L,
                100,
                User.Role.USER,
                item7);
        userRepository.save(user7);
        Point point7 = new Point("회원가입",500L,500L,user7);
        pointRepository.save(point7);
    }
    }


