package com.dalk.dto.responseDto;

import com.dalk.domain.wl.Agree;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgreeResponseDto {
    private Boolean isAgree;

    public AgreeResponseDto (Agree agree){
        this.isAgree = agree.getIsAgree();
    }
}
