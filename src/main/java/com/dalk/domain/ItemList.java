package com.dalk.domain;

import lombok.Getter;

@Getter
public enum ItemList {
    ONLYME("onlyMe", 1000);

    private final String name;
    private final int price;

    ItemList(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
