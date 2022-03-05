package com.dalk.repository;

import com.dalk.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findTopByUserId(Long id);
}
