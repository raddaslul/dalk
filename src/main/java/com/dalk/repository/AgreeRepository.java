package com.dalk.repository;

import com.dalk.domain.Agree;
import com.dalk.domain.Comment;
import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreeRepository extends JpaRepository<Agree , Long> {
    Optional<Agree> findByUserAndComment(User user, Comment comment);

    Optional<Agree> findByComment(Comment comment);
}
