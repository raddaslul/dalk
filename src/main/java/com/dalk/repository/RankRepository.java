package com.dalk.repository;

import com.dalk.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Ranking,Long> {
}
