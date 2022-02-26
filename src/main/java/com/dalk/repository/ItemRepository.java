package com.dalk.repository;

import com.dalk.domain.Item;
import com.dalk.dto.responseDto.ItemResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByUserId(Long id);
}