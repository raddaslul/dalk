package com.dalk.repository;

import com.dalk.domain.Category;
import com.dalk.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoryByChatRoom(ChatRoom chatRoom);

    List<Category> findCategoryByChatRoom_Id(Long chatRoomId);

    List<Category> findCategoryByBoard_Id(Long boardId);

    List<Category> findAllByChatRoom(ChatRoom chatRoom);
}
