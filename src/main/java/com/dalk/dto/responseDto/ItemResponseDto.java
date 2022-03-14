package com.dalk.dto.responseDto;

import com.dalk.domain.ItemType;
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
        this.bigFont = MinkiService.changeItem(user, ItemType.bigFont);
        this.onlyMe = MinkiService.changeItem(user, ItemType.onlyMe);
        this.myName=MinkiService.changeItem(user, ItemType.myName);
        this.papago=MinkiService.changeItem(user, ItemType.papago);
        this.reverse=MinkiService.changeItem(user, ItemType.reverse);
    }
}
