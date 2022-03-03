package com.dalk.repository;

import com.dalk.domain.Points;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointsRepository extends JpaRepository<Points,Long> {
    Points findbyorderbycreatedAtbydesc();
}
