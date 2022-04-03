package com.dalk.repository;

import com.dalk.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findDistinctByCategorys_CategoryOrTopicAContainingIgnoreCaseOrTopicBContainingIgnoreCaseOrderByCreatedAtDesc(String category1, String category2, String category3, Pageable pageable);

    Page<Board> findDistinctByCategorys_CategoryOrderByCreatedAtDesc(String category1, Pageable pageable);

    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Board> findAllByOrderByCreatedAtDesc();

    List<Board> findAllByUser_Id(Long userId);
}