package com.dalk.repository;

import com.dalk.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByUser_IdAndItemCode(Long userId, String item);
}
