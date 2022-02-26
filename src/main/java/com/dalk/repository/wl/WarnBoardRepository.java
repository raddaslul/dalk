package com.dalk.repository.wl;

import com.dalk.domain.wl.WarnBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarnBoardRepository extends JpaRepository<WarnBoard, Long> {
}