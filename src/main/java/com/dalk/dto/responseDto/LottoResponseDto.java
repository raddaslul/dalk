package com.dalk.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LottoResponseDto {
    private String rank;
    private Long count;

    public LottoResponseDto(String rank, Long count) {
        this.rank = rank;
        this.count = count;
    }
}
