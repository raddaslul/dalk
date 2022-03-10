package com.dalk.repository;

import com.dalk.domain.Board;
import com.dalk.domain.Category;
import com.dalk.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoryByChatRoom(ChatRoom chatRoom);

    List<Category> findCategoryByChatRoom_Id(Long Id);

    List<Category> findCategoryByBoard(Board board);

    List<Category> findAllByChatRoom(ChatRoom chatRoom);
}
