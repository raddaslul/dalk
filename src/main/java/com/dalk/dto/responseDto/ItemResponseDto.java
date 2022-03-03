package com.dalk.dto.responseDto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class ItemResponseDto {
    private String itemName;
    private Integer quantity;

    public ItemResponseDto(String itemName, Integer quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }
}
