package com.dalk.repository;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.ChatRoomUser;
import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long>{
    List<ChatRoomUser> findAllByChatRoom_Id(Long roomId);
    ChatRoomUser findByChatRoom_IdAndUser_Id(Long roomId, Long userId);
    void deleteByChatRoom(ChatRoom chatRoom);
    void deleteByUser_Id(Long userId);
}
