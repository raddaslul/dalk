package com.dalk.dto.responseDto;

import com.dalk.domain.User;
import com.dalk.service.MinkiService;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemResponseDto {
    private Long bigFont;
    private Long onlyMe;
    private Long myName;
    private Long papago;
    private Long reverse;

    public ItemResponseDto(User user) {
        this.bigFont = MinkiService.changeBigFont(user);
        this.onlyMe = MinkiService.changeOnlyMe(user);
        this.myName=MinkiService.changeMyName(user);
        this.papago=MinkiService.changePapago(user);
        this.reverse=MinkiService.changeReverse(user);
    }
}
