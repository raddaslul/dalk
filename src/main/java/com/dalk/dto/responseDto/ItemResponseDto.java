package com.dalk.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ItemResponseDto implements Serializable {
    private final Boolean bold;
    private final String color;
}
