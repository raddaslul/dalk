package com.dalk.repository;

import com.dalk.domain.Board;
import com.dalk.domain.Category;
import com.dalk.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoryByChatRoom(ChatRoom chatRoom);

    List<Category> findCategoryByChatRoom_Id(Long Id);

    List<Category> findCategoryByBoard(Board board);

//    List<Category> findCategoryByBoard_Id(Long Id);

    List<Category> findAllByChatRoom_TopicAContainingIgnoreCaseOrChatRoom_TopicBContainingIgnoreCaseOrCategory(String keyword1, String keyword2, String keyword3);

//    List<Category> findAllByChatRoom_TopicAContainingIgnoreCaseOrChatRoom_TopicBContainingIgnoreCase(String keyword1, String keyword2);

//    List<Category> findAllByChatRoom_TopicAContainingIgnoreCase(String keyword1);
//    List<Category> findAllByChatRoom_TopicBContainingIgnoreCase(String keyword1);

    List<Category> findAllByCategory(String keyowrd);

    List<Category> findAllByBoard_TopicAContainingIgnoreCaseOrBoard_TopicBContainingIgnoreCaseOrCategory(String keyword1, String keyword2, String keyword3);

//    @Modifying
//    @Query("select p from Category p where p.category like %:keyword% or p.chatRoom.topicA like %:keyword% or p.chatRoom.topicB like %:keyword%")
//    List<Category> findSearch(String keyword);

    List<Category> findAllByChatRoom(ChatRoom chatRoom);

    List<Category> findAllByChatRoomId(Long id);
}
