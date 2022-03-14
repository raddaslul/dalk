package com.dalk.service;

import com.dalk.domain.Item;
import com.dalk.domain.ItemType;
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

    public static Long changeItem(User user, ItemType itemType) {
        Item item = itemRepository.findByUser_IdAndItemCode(user.getId(), itemType.getItemCode());
        return item.getCnt();
    }
}
