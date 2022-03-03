package com.dalk.domain;

import lombok.Getter;

@Getter
public enum ItemList {
    SIGNUP("signUp", 1000),
    ONLYME("onlyMe", 1000),
    BIGFONT("bigFont", 1000),
    MYNAME("myName", 1000);
//    EXBUY("exBuy", 1000);

    private final String name;
    private final int price;

    ItemList(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
