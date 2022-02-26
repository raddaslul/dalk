package com.dalk.repository.wl;

import com.dalk.domain.wl.WarnComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarnCommentRepository extends JpaRepository<WarnComment, Long> {
}