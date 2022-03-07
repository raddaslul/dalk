package com.dalk.repository;
import com.dalk.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByOrderByCreatedAtDesc();
    List<ChatRoom> findTop6ByOrderByCreatedAtDesc();
    List<ChatRoom> findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(String category1 ,String category2, String category3);
    List<ChatRoom> findAllByStatus(Boolean status);

}
