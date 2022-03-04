package com.dalk.repository;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findTopByUserIdOrderByCreatedAtDesc(Long userId);

    List<Point> findById(Optional<User> user);

}
