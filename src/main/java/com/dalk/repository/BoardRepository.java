package com.dalk.repository;

import com.dalk.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCase(String category1 ,String category2, String category3);

    List<Board> findAllByOrderByCreatedAtDesc();

}