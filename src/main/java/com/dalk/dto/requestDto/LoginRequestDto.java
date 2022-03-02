package com.dalk.dto.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor  // 기본 생성자 추가
public class LoginRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @ApiModelProperty(value = "아이디")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @ApiModelProperty(value = "비밀번호")
    private String password;

    public LoginRequestDto (String username, String password){
        this.username = username;
        this.password = password;
    }
}
