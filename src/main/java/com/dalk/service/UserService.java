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
import com.dalk.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//    private final WarnUserRepository warnUserRepository;

    private final Long onlyMePrice = 100L;
    private final Long bigFontPrice = 100L;
    private final Long myNamePrice = 100L;
    private final Integer exBuyPrice = 100;

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


        Item item = new Item(0, 0, 0);
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


    public void buyItem(String item, User user) {
        Item buyitem = itemRepository.findById(user.getItem().getId()).orElseThrow(
                () -> new ItemNotFoundException("아이템이 없습니다")
        );
//        Point recentPoint = pointRepository.findTopByUserIdOrderByCreatedAt(user.getId()); //얘는 왜 예외처리 안뜸?
        switch (item) {
            case "onlyMe":
                buyitem.setOnlyMe(buyitem.getOnlyMe() + 1);
                itemRepository.save(buyitem);
                itemBuy(user, onlyMePrice, "나만 말하기");
                break;
            case "bigFont":
                buyitem.setBigFont(buyitem.getBigFont() + 1);
                itemRepository.save(buyitem);
                itemBuy(user, bigFontPrice, "내글자 크게하기");
                break;
            case "myName":
                buyitem.setMyName(buyitem.getMyName() + 1);
                itemRepository.save(buyitem);
                itemBuy(user, myNamePrice, "모두 내이름으로 바꾸기");
                break;
            case "exBuy":
                user.setEx(user.getEx()+exBuyPrice);
                userRepository.save(user);
                itemBuy(user, Long.valueOf(exBuyPrice),"경험치");
                break;
        }
    }

    private void itemBuy(User user, Long price, String item) {
        if (user.getTotalPoint() >= price) {
            user.setTotalPoint(user.getTotalPoint()-price);
            userRepository.save(user);
            Point point = new Point(item + " 구매", -price, user.getTotalPoint(), user);
            pointRepository.save(point);
        } else {
            throw new LackPointException("보유한 포인트가 부족합니다");
        }
    }

    public void useItem(String item, User user) {
        Item useitem = itemRepository.findById(user.getItem().getId()).orElseThrow(
                () -> new ItemNotFoundException("아이템이 없습니다")
        );
        switch (item) {
            case "onlyMe":
                if(useitem.getOnlyMe()>=1) {
                    useitem.setOnlyMe(useitem.getOnlyMe() - 1);
                    itemRepository.save(useitem);
                }else {
                   throw  new ItemNotFoundException("아이템이 없습니다");
                }
                break;
            case "bigFont":
                if(useitem.getBigFont()>=1) {
                    useitem.setBigFont(useitem.getBigFont() - 1);
                    itemRepository.save(useitem);
                }else {
                    throw  new ItemNotFoundException("아이템이 없습니다");
                }
                break;
            case "myName":
                if(useitem.getMyName()>=1) {
                    useitem.setMyName(useitem.getMyName() - 1);
                    itemRepository.save(useitem);
                }else {
                    throw  new ItemNotFoundException("아이템이 없습니다");
                }
                break;
        }
    }

//    public WarnUserResponseDto WarnUser(Long userId, UserDetailsImpl userDetails) {
//
//        User user1 = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
//                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
//        );
//
//        String warnUserName = String.valueOf(userRepository.findById(userId).orElseThrow(
//                ()-> new LoginUserNotFoundException("유저가 존재하지 않습니다. ")
//        ));
//
//        WarnUserResponseDto warnUserResponseDto = new WarnUserResponseDto();
//
//
//        WarnUser warnUserCheck = warnUserRepository.findByIsWarnAndUserIdAndWarnUserId(false,user1,warnUserName).orElse(null);
//
//            if(warnUserCheck == null){
//                WarnUser warnUser = new WarnUser(true,warnUserName,user1);
//                warnUserRepository.save(warnUser);
//                warnUserResponseDto.setWarnUserName(warnUser.getWarnUserName());
//                warnUserResponseDto.setWarn(warnUser.getIsWarn());
//                return warnUserResponseDto;
//            }else {
//                return null;
//            }
//
//    }
}

