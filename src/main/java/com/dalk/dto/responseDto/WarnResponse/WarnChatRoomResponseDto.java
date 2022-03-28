package com.dalk.dto.responseDto.WarnResponse;

import com.dalk.domain.ChatRoom;
import lombok.Data;

@Data
public class WarnChatRoomResponseDto {
    private Long roomId;
    private String topicA;
    private String topicB;
    private Integer chatRoomWarnCnt;

    public WarnChatRoomResponseDto(ChatRoom chatRoom, Integer chatRoomWarnCnt) {
        this.roomId = chatRoom.getId();
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
        this.chatRoomWarnCnt = chatRoomWarnCnt;
    }
}
