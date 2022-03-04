package com.dalk;

import com.dalk.domain.*;
import com.dalk.repository.*;
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
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final PointRepository pointRepository;

    @Override
    public void run(ApplicationArguments args) {
        String encPassword = passwordEncoder.encode("adminPass");
        User admin = new User(
                "adminUser",
                encPassword,
                "adminNick",
                100L,
                User.Role.ADMIN);
        userRepository.save(admin);

        User user1 = new User( //유저추가
                "user1",
                encPassword,
                "user1",
                10L,
                User.Role.USER);
        userRepository.save(user1);

        Board board1 = new Board( //게시글 추가
                "짜장",
                "짬뽕",
                "승자",
                "내용",
                "카테고리",
                user1
        );
        Board board2 = new Board(
                "짬뽕",
                "짜장",
                "승자2",
                "내용2",
                "카테고리2",
                user1
        );
        Board board3 = new Board(
                "신동석",
                "김영민",
                "김영민",
                "내용2",
                "카테고리2",
                user1
        );
        Board board4 = new Board(
                "aBc",
                "aaaAAA",
                "aBc",
                "내용3",
                "카테고리",
                user1
        );
        Board board5 = new Board(
                "AAAaaa",
                "AbC",
                "aBc",
                "내용3",
                "카테고리",
                user1
        );
        boardRepository.save(board1);
        boardRepository.save(board2);
        boardRepository.save(board3);
        boardRepository.save(board4);
        boardRepository.save(board5);
        Comment comment1 = new Comment(
                "안녕하세요1",
                board1,
                user1
        );
        Comment comment2 = new Comment(
                "안녕하세요2",
                board1,
                user1
        );
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        Item item1 = new Item(
                admin
        );

        Item item2 = new Item(
                user1
        );
        itemRepository.save(item1);
        itemRepository.save(item2);


        Point point1 = new Point(
                "회원가입",
                500L,
                500L,
                user1
        );
        pointRepository.save(point1);

        Point point2 = new Point(
                "회원가입",
                50000L,
                50000L,
                admin
        );
        pointRepository.save(point2);

    }

}
