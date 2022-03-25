package com.dalk.dto.requestDto;

import lombok.Data;

@Data
public class GivePointRequestDto {
    String username;
    Long point;
    String content;
}
