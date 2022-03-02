package com.dalk.repository;


import com.dalk.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByOrderByCreatedAtDesc();
    List<ChatRoom> findByCategory(String category);
    List<ChatRoom> findTop6ByOrderByCreatedAtTimeDesc();
}
