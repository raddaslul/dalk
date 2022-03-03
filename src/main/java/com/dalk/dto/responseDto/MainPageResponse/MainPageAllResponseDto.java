package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.Board;
import com.dalk.domain.ChatRoom;
import com.dalk.domain.time.TimeConversion;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainPageAllResponseDto {
    private UserInfoResponseDto userInfo;
    private Long roomId;
    private String topicA;
    private String topicB;
    private String content;
    private String category;
    private Long restTime;
    private String createdAt;
    private Boolean time;

    public MainPageAllResponseDto(ChatRoom chatRoom) {
        this.userInfo = new UserInfoResponseDto(chatRoom.getUser());
        this.roomId = chatRoom.getId();
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
        this.content = chatRoom.getContent();
        this.category = chatRoom.getCategory();
        this.restTime = TimeConversion.restTime(chatRoom.getCreatedAt(),chatRoom.getTime());
        this.createdAt = TimeConversion.timeCreatedConversion(chatRoom.getCreatedAt());
        this.time = chatRoom.getTime();
    }
}
