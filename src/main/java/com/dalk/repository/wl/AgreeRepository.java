package com.dalk.repository.wl;

import com.dalk.domain.Comment;
import com.dalk.domain.User;
import com.dalk.domain.wl.Agree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgreeRepository extends JpaRepository<Agree , Long> {
    Optional<Agree> findByUserAndComment(User user, Comment comment);

    List<Agree> findByCommentId(Long id);
}
