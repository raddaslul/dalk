package com.dalk.service;

import com.dalk.domain.Item;
import com.dalk.domain.Lotto;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.domain.wl.WarnUser;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnBoardResponseDto;
import com.dalk.dto.responseDto.WarnResponse.WarnUserResponseDto;
import com.dalk.exception.ex.*;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.LottoRepository;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import com.dalk.repository.wl.WarnCommentRepository;
import com.dalk.repository.wl.WarnUserRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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


        Item item = new Item(0,0,0, 0, 0);
        itemRepository.save(item);

        User user = new User(username, password, nickname, 500L, 1, User.Role.USER, item);
        userRepository.save(user);

        item.setUser(user);
        itemRepository.save(item);

        Point point = new Point("회원가입 지급", 500L, 500L, user);
        pointRepository.save(point);

        Lotto lotto = new Lotto(0L, user);
        lottoRepository.save(lotto);
    }

    // 채팅방에서 유저 확인하기
    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new LoginUserNotFoundException("찾는 유저가 없습니다")
        );
        return user;
    }


    @Transactional
    public WarnUserResponseDto WarnUser(Long userId, UserDetailsImpl userDetails) {
        User user1 = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
        );

        String warnUserName = String.valueOf(userRepository.findById(userId).orElseThrow(
                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
        ));
        WarnUserResponseDto warnUserResponseDto = new WarnUserResponseDto();
        WarnUser warnUserCheck = warnUserRepository.findByUserIdAndWarnUserName(userDetails.getUser().getId(),warnUserName).orElse(null);

            if(warnUserCheck == null){
                WarnUser warnUser = new WarnUser(true,warnUserName,user1);
                warnUserRepository.save(warnUser);
                warnUserResponseDto.setWarnUserName(warnUser.getWarnUserName());
                warnUserResponseDto.setWarn(warnUser.getIsWarn());
                return warnUserResponseDto;
            }else {
                return null;
            }

    }
}

