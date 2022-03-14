package com.dalk.dto.responseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LottoResponseDto {
    private Integer rank;
    private Integer count;

    public LottoResponseDto(Integer rank, Integer count) {
        this.rank = rank;
        this.count = count;
    }
}
