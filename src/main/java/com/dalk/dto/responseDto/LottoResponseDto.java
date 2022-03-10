package com.dalk.dto.responseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LottoResponseDto {
    private Integer rank;
    private Long count;

    public LottoResponseDto(Integer rank, Long count) {
        this.rank = rank;
        this.count = count;
    }
}
