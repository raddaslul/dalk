package com.dalk.dto.responseDto;

import com.dalk.domain.Item;
import com.dalk.domain.User;
import com.dalk.security.UserDetailsImpl;
import lombok.*;

import java.io.Serializable;

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
