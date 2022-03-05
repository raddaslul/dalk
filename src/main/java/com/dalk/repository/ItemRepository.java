package com.dalk.repository;

import com.dalk.domain.Item;
import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}