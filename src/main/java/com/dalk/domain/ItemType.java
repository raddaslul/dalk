package com.dalk.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum ItemType {
        bigFont("bigFont", 100L, "내 글자 크게 만들기"),
        onlyMe("onlyMe", 100L, "나만 말하기"),
        myName("myName", 100L, "모두 내이름으로 바꾸기"),
        papago("papago", 100L, "나만빼고 랜덤으로 번역하기"),
        reverse("reverse", 100L, "나만빼고 반대로 말하기"),
        exBuy("exBuy", 100L, "경험치 구매");
        private String itemCode;
        private Long price;
        private String itemName;

        ItemType(String itemCode, Long price, String itemname) {
            this.itemCode = itemCode;
            this.price = price;
            this.itemName = itemname;

    }

}
