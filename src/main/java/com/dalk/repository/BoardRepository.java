package com.dalk.repository;

import com.dalk.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

//    @Modifying
//    @Query("select p from Board p where p.topicA like %:keyword% or p.topicB like %:keyword%")
//    List<Board> findSearch(String keyword);
    List<Board> findAllByTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(String keyword1, String keyword2);

}