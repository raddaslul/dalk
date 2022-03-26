package com.dalk.dto.responseDto.MainPageResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoteResultResponseDto {
    private String topic;
    private String rate;
    private String totalPoint;
    private String cnt;
    private String topPoint;
}
