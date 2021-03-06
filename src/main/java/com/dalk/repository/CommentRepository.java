package com.dalk.repository;

import com.dalk.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoard_Id(Long boardId);

    List<Comment> findAllByUser_Id(Long userId);
}