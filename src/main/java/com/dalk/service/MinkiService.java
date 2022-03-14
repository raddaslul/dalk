package com.dalk.service;

import com.dalk.domain.Item;
import com.dalk.domain.User;
import com.dalk.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MinkiService {

    public static ItemRepository itemRepository;

    @Autowired
    public MinkiService(ItemRepository itemRepository) {
        MinkiService.itemRepository = itemRepository;
    }

    public static Long changeBigFont(User user) {
        Item itemTests = itemRepository.findByItemCodeAndUser("bigFont", user);
        return itemTests.getCnt();
    }
    public static Long changeOnlyMe(User user) {
        Item itemTests = itemRepository.findByItemCodeAndUser("onlyMe", user);
        return itemTests.getCnt();
    }

    public static Long changeMyName(User user) {
        Item itemTests = itemRepository.findByItemCodeAndUser("myName", user);
        return itemTests.getCnt();
    }

    public static Long changePapago(User user) {
        Item itemTests = itemRepository.findByItemCodeAndUser("papago", user);
        return itemTests.getCnt();
    }
    public static Long changeReverse(User user) {
        Item itemTests = itemRepository.findByItemCodeAndUser("reverse", user);
        return itemTests.getCnt();
    }
    public static Long changeExBuy(User user) {
        Item itemTests = itemRepository.findByItemCodeAndUser("exBuy", user);
        return itemTests.getCnt();
    }
}
