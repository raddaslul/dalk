package com.dalk.dto.responseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LottoResponseDto {
    private Integer rank;
    private Integer cnt;

    public LottoResponseDto(Integer rank, Integer cnt) {
        this.rank = rank;
        this.cnt = cnt;
    }
}
