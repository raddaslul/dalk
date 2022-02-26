package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class UserInfoResponseDto {
    
    private Long id;
    private String username;
    private String nickname;
    private Long point;
    private Integer level;
    private User.Role role;
    private ItemResponseDto item;
}
