package com.dalk.repository;

import com.dalk.domain.Point;
import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findAllByUserOrderByCreatedAtDesc(User user);
}
