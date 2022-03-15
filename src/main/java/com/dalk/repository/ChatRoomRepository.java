package com.dalk.repository;

import com.dalk.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Page<ChatRoom> findAllByOrderByCreatedAtDesc(Pageable pageable);
    List<ChatRoom> findAllByOrderByCreatedAtDesc();

    List<ChatRoom> findTop6ByOrderByCreatedAtDesc();
    Page<ChatRoom> findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(String category1 ,String category2, String category3,Pageable pageable);
}
