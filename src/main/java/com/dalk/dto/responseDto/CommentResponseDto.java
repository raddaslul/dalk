package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private UserInfoResponseDto userInfo;
    private Long userId;
    private String username;
    private String nickname;
    private Long point;
    private Integer level;
    private User.Role role;
    private ItemResponseDto item;

}
