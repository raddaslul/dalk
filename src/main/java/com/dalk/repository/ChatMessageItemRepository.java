package com.dalk.repository;

import com.dalk.domain.ChatMessageItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageItemRepository extends JpaRepository<ChatMessageItem, Long> {

    ChatMessageItem findByRoomId(String roomId);
}
