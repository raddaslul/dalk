package com.dalk.service;

import com.dalk.domain.Item;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.exception.ex.DuplicateUsernameException;
import com.dalk.exception.ex.DuplicationNicknameException;
import com.dalk.exception.ex.ItemNotFoundException;
import com.dalk.exception.ex.LoginUserNotFoundException;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PointRepository pointRepository;

    //회원가입
    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new DuplicateUsernameException("중복된 사용자 ID 가 존재합니다.");
        }

        String nickname = requestDto.getNickname();
        Optional<User> found1 = userRepository.findByNickname(nickname);
        if (found1.isPresent()) {
            throw new DuplicationNicknameException("중복된 사용자 닉네임이 존재합니다.");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());//비번 인코딩


        Item item = new Item(0,0,0);
        itemRepository.save(item);

        User user = new User(username, password, nickname,500L,1, User.Role.USER, item);
        userRepository.save(user);

        Point point = new Point("회원가입",500L,500L, user);
        pointRepository.save(point);
    }

    // 채팅방에서 유저 확인하기
    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new LoginUserNotFoundException("찾는 유저가 없습니다")
        );
        return user;
    }


    public void buyItem(String item, User user) {
        Item buyitem = itemRepository.findById(user.getItem().getId()).orElseThrow(
                ()-> new ItemNotFoundException("아이템이 없습니다")
        );
//        Point recentPoint = pointRepository.findTopByUserIdOrderByCreatedAt(user.getId()); //얘는 왜 예외처리 안뜸?
        if (item.equals("{onlyMe}")) {
            buyitem.setOnlyMe(buyitem.getOnlyMe()+1);
            pointUpdate(user);
        } else if (item.equals("{bigFont}")) {
            buyitem.setBigFont(buyitem.getBigFont()+1);
            pointUpdate(user);
        } else if (item.equals("{myName}")) {
            buyitem.setMyName(buyitem.getMyName() + 1);
            pointUpdate(user);
        }
    }

    private void pointUpdate(User user) {  //추후 아이템 가격 변동여부에 따라 변화할 수 있음
        Long totalPoint = user.getTotalPoint();
        Point point = new Point("아이템 구매", -100L, totalPoint-100L, user);
        pointRepository.save(point);
        user.setTotalPoint(totalPoint-100L);
        userRepository.save(user);

    }

}

