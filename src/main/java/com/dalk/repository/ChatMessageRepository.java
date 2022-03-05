package com.dalk.repository;

import com.dalk.domain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findByRoomId(String roomId);
//    Page<ChatMessage> findByRoomId(String roomId, Pageable pageable);
}
