package com.dalk.dto.responseDto;

import com.dalk.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ItemResponseDto {
    private Boolean bold;
    private String color;

    public ItemResponseDto(Item item) {
        this.bold = item.getBold();
        this.color = item.getColor();
    }
}
