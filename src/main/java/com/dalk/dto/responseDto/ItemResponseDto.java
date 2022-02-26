package com.dalk.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class ItemResponseDto {
    private Boolean bold;
    private String color;
}
