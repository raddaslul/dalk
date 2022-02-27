package com.dalk.dto.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class SignupRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    @ApiModelProperty(value = "아이디")
    public String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @ApiModelProperty(value = "비밀번호")
    public String password;

    @NotBlank(message = "비밀번호를 한번 더 입력해주세요.")
    @ApiModelProperty(value = "비밀번호 확인")
    public String passwordCheck;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @ApiModelProperty(value = "닉네임")
    public String nickname;
}
