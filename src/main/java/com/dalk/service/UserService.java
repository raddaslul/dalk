package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.domain.wl.WarnUser;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.exception.ex.*;
import com.dalk.repository.*;
import com.dalk.repository.wl.WarnUserRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final LottoRepository lottoRepository;
    private final WarnUserRepository warnUserRepository;



    //회원가입
    @Transactional
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


        User user = new User(username, password, nickname, 5000L, 0,0, User.Role.USER);
        userRepository.save(user);

        List<Item> items = getItemTests(user);

        user.setItems(items);
        userRepository.save(user);

        Point point = new Point("회원가입 지급", 5000L, 5000L, user);
        pointRepository.save(point);

        Lotto lotto = new Lotto(0, user);
        lottoRepository.save(lotto);
    }

    public List<Item> getItemTests(User user) {
        List<Item> items = new ArrayList<>();
        Item item1 = new Item("bigFont", 1L, user);
        Item item2 = new Item("onlyMe", 1L, user);
        Item item3 = new Item("myName", 1L, user);
        Item item4 = new Item("papago", 1L, user);
        Item item5 = new Item("reverse", 1L, user);
        Item item6 = new Item("exBuy", 1L, user);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
        itemRepository.save(item5);
        itemRepository.save(item6);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        return items;
    }

    // 채팅방에서 유저 확인하기
    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new LoginUserNotFoundException("찾는 유저가 없습니다")
        );
        return user;
    }


    @Transactional
    public HashMap<String, Object> WarnUser(Long userId, UserDetailsImpl userDetails) {
        User user1 = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
        );
        User user2 = userRepository.findById(userId).orElseThrow(
                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
        );
        WarnUser warnUserCheck = warnUserRepository.findByUserIdAndWarnUserName(user1.getId(),user2.getUsername()).orElse(null);

      HashMap<String, Object> result = new HashMap<>();
            if(warnUserCheck == null){
                WarnUser warnUser = new WarnUser(user1,user2.getUsername());
                warnUserRepository.save(warnUser);
                user2.setWarnUserCnt(user2.getWarnUserCnt()+1);
                result.put("result", "true");
                return result;
            }
            else throw new WarnDuplicateException("이미 신고한 유저입니다.");
    }

    public static UserInfoResponseDto userInfo(User user) {
        return new UserInfoResponseDto(user);
    }
}

