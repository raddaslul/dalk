package com.dalk;

import com.dalk.domain.Board;
import com.dalk.domain.User;
import com.dalk.repository.BoardRepository;
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
    private final BoardRepository boardRepository;

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

        Board board1 = new Board(
                "짜장",
                "짬뽕",
                "승자",
                "내용",
                "카테고리"
        );
        Board board2 = new Board(
                "짬뽕",
                "짜장",
                "승자2",
                "내용2",
                "카테고리2"
        );
        Board board3 = new Board(
                "신동석",
                "김영민",
                "김영민",
                "내용2",
                "카테고리2"
        );
        boardRepository.save(board1);
        boardRepository.save(board2);
        boardRepository.save(board3);
    }


}
