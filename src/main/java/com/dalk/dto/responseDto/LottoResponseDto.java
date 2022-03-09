package com.dalk.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LottoResponseDto {
    private String rank;

    public LottoResponseDto(String rank) {
        this.rank = rank;
    }
}
