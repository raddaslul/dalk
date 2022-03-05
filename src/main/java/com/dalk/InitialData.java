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
        User admin = new User(
                "adminUser",
                encPassword,
                "adminNick",
                500L,
                100,
                User.Role.ADMIN,
                item1);
        userRepository.save(admin);
        Point point1 = new Point("회원가입",500L,500L,admin);
        pointRepository.save(point1);


        Item item2 = new Item(0,0,0);
        itemRepository.save(item2);
        User user1 = new User( //유저추가
                "user1",
                encPassword,
                "user1",
                500L,
                100,
                User.Role.USER,
                item2);
        userRepository.save(user1);
        Point point2 = new Point("회원가입",500L,500L,user1);
        pointRepository.save(point2);

//        Board board1 = new Board( //게시글 추가
//                "짜장",
//                "짬뽕",
//                "승자",
//                "내용",
//                user1
//        );
//        List<String> categoryList1 = new ArrayList<>();
//        categoryList1.add("연애");
//        categoryList1.add("오락");
//        for(String stringCategory : categoryList1){
//            Category category = new Category(board1, stringCategory);
//            categoryRepository.save(category);
//        }
//
//        Board board2 = new Board(
//                "짬뽕",
//                "짜장",
//                "승자2",
//                "내용2",
//                user1
//        );
//        List<String> categoryList2 = new ArrayList<>();
//        categoryList2.add("도움");
//        categoryList2.add("사랑");
//        for(String stringCategory : categoryList2){
//            Category category = new Category(board1, stringCategory);
//            categoryRepository.save(category);
//        }
//        boardRepository.save(board1);
//        boardRepository.save(board2);
//
//        Comment comment1 = new Comment(
//                "안녕하세요1",
//                board1,
//                user1
//        );
//        Comment comment2 = new Comment(
//                "안녕하세요2",
//                board1,
//                user1
//        );
//        commentRepository.save(comment1);
//        commentRepository.save(comment2);
    }

}
