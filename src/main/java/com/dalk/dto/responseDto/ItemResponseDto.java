package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemResponseDto {
    private Integer bigFont;
    private Integer onlyMe;
    private Integer myName;

    public ItemResponseDto(User user) {
        this.bigFont=user.getItem().getBigFont();
        this.onlyMe=user.getItem().getOnlyMe();
        this.myName=user.getItem().getMyName();
    }
}
