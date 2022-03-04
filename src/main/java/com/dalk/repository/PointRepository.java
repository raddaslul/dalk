package com.dalk.repository;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findTopByUser(User user);
}
