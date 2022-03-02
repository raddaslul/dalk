package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemResponseDto {
    private Boolean bold;
    private String color;

    public ItemResponseDto(User user) {
        this.bold = user.getItem().getBold();
        this.color = user.getItem().getColor();
    }
}
