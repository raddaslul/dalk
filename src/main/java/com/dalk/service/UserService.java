package com.dalk.service;

import com.dalk.domain.Item;
import com.dalk.domain.Points;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.PointsResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.PointsRepository;
import com.dalk.repository.UserRepository;
import com.dalk.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PointsRepository pointsRepository;

    //회원가입
    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }

        String nickname = requestDto.getNickname();
        Optional<User> found1 = userRepository.findByNickname(nickname);
        if (found1.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 닉네임이 존재합니다.");
        }

        String password = passwordEncoder.encode(requestDto.getPassword());//비번 인코딩

        User user = new User(username, password, nickname);
        user.setLevel(1);
        user.setRole(User.Role.USER);
        userRepository.save(user);

        Item item = new Item(false, "black", user);
        itemRepository.save(item);

        Points points = new Points(500L,500L,"회원가입",user);
//        Points points = new Points();
//        points.getUser(points.getChangepoint(),points.getTotalpoints(),points.getStatus());
//        points.getUser().getPoints();
//        points.settotalpoints(500L);
//        points.setstatus("회원가입");
//        points.getUser().getId();
//        user.

        pointsRepository.save(points);

    }
//   유저정보
    public UserInfoResponseDto userInfo(UserDetailsImpl userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                ()-> new IllegalArgumentException("유저가 없습니다.")
        );
        ItemResponseDto itemResponseDto = new ItemResponseDto(user);
        Points points = pointsRepository.findbyorderbycreatedAtbydesc(user);
//                /findtop1byorderbycreatedatdesc
        PointsResponseDto pointsResponseDto = new PointsResponseDto(user);
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getPoints(),
                user.getLevel(),
                user.getRole(),
                itemResponseDto
                );
        System.out.println(itemResponseDto);
        System.out.println(pointsResponseDto);
        System.out.println(userInfoResponseDto);
        return userInfoResponseDto;
    }

//    private PointsResponseDto pointsResponseDto(User user){
//
//
//
//    }




}

