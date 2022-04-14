package com.dalk.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum ItemType {
    bigFont("bigFont", 1000L, "내 글자 크게 만들기"),
    onlyMe("onlyMe", 1000L, "나만 말하기"),
    myName("myName", 1000L, "모두 내이름으로 바꾸기"),
    papago("papago", 2000L, "나만빼고 랜덤으로 번역하기"),
    reverse("reverse", 1500L, "나만빼고 반대로 말하기"),
    exBuy("exBuy", 500L, "경험치");
    private String itemCode;
    private Long price;
    private String itemName;

    ItemType(String itemCode, Long price, String itemName) {
        this.itemCode = itemCode;
        this.price = price;
        this.itemName = itemName;
    }
}
