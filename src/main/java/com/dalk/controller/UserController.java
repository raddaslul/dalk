package com.dalk.controller;



import com.dalk.domain.Item;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.requestDto.SignupRequestDto;
import com.dalk.dto.responseDto.ItemResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.PointRepository;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PointRepository pointRepository;
    private final ItemRepository itemRepository;

    // 회원가입
    @PostMapping("/users/signup")
    @ApiOperation(value = "회원가입")
    public String signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return "redirect:/users/login";
    }

    //로그인 확인
    @GetMapping("/loginCheck")
    @ApiOperation(value = "로그인확인")
    public UserInfoResponseDto userInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Point point = pointRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId());

        List<ItemResponseDto> items = new ArrayList<>();
        for (ItemResponseDto itemResponseDto : items) {
            Item item = itemRepository.findByUser(user);
            String itemName = item.getItemName();
            Integer quantity = item.getQuantity();
            itemResponseDto = new ItemResponseDto(itemName, quantity);
            items.add(itemResponseDto);
        }
        return new UserInfoResponseDto(user, point, items);
    }
}