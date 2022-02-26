package com.dalk.dto.requestDto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequestDto {
    private String username;
    private String password;
    private String passwordCheck;
    private String nickname;
}
