package com.dalk.repository;

import com.dalk.domain.Item;
import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByItemCodeAndUser(String item, User user);

    List<Item> findAllByUser(User user);
}
