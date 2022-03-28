package com.dalk.repository;

import com.dalk.domain.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {

    Vote findByChatRoom_Id(Long chatRoomId);
}
