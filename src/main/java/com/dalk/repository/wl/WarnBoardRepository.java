package com.dalk.repository.wl;

import com.dalk.domain.Board;
import com.dalk.domain.wl.WarnBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarnBoardRepository extends JpaRepository<WarnBoard, Long> {

    Optional<WarnBoard> findByUserIdAndBoard(Long user_id, Board board);
}