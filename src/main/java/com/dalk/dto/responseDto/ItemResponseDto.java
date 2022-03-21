package com.dalk.dto.responseDto;

import com.dalk.domain.ItemType;
import com.dalk.domain.User;
import com.dalk.service.StaticService;
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
        this.bigFont = StaticService.changeItem(user, ItemType.bigFont);
        this.onlyMe = StaticService.changeItem(user, ItemType.onlyMe);
        this.myName= StaticService.changeItem(user, ItemType.myName);
        this.papago= StaticService.changeItem(user, ItemType.papago);
        this.reverse= StaticService.changeItem(user, ItemType.reverse);
    }
}
