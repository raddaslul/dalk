package com.dalk.repository;

import com.dalk.domain.vote.SaveVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaveVoteRepository extends JpaRepository<SaveVote, Long> {

    SaveVote findByUser_IdAndChatRoom_IdAndVote_Id(Long id1, Long id2, Long id3);

    List<SaveVote> findAllByChatRoom_Id(Long ChatRoomId);

    List<SaveVote> findAllByChatRoom_IdAndPickTrue(Long ChatRoomId);

    List<SaveVote> findAllByChatRoom_IdAndPickFalse(Long ChatRoomId);

    SaveVote findByUser_IdAndChatRoom_Id(Long userId, Long ChatRoomId);

    List<SaveVote> findAllByUser_Id(Long userId);
}
