package com.dalk.repository.wl;

import com.dalk.domain.wl.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}