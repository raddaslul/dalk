package com.dalk.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RankDto {
    private String nickname;
    private Integer ex;
}
