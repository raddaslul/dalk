package com.dalk.repository.wl;


import com.dalk.domain.Comment;
import com.dalk.domain.wl.WarnComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarnCommentRepository extends JpaRepository<WarnComment, Long> {
    Optional<WarnComment> findByUserIdAndComment(Long user_id, Comment comment);


    List<WarnComment> findByCommentId(Long commentId);
}