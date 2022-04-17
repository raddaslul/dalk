package com.dalk.repository.wl;

import com.dalk.domain.wl.WarnChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarnChatRoomRepository extends JpaRepository<WarnChatRoom, Long> {

    Optional<WarnChatRoom> findById(Long aLong);

    Optional<WarnChatRoom> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);
}