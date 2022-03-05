package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.time.TimeConversion;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.service.MinkiService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class MainPageAllResponseDto {
    private UserInfoResponseDto userInfo;
    private Long roomId;
    private String topicA;
    private String topicB;
    private String content;
    private List<String> categoryList;
    private Long restTime;
    private String createdAt;
    private Boolean time;

    public MainPageAllResponseDto(ChatRoom chatRoom, List<String> categoryList) {
//        this.userInfo = new UserInfoResponseDto(chatRoom.getUser());
        this.userInfo = MinkiService.userInfo(chatRoom.getUser());
        this.roomId = chatRoom.getId();
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
        this.content = chatRoom.getContent();
        this.categoryList = categoryList;
        this.restTime = TimeConversion.restTime(chatRoom.getCreatedAt(),chatRoom.getTime());
        this.createdAt = TimeConversion.timeCreatedConversion(chatRoom.getCreatedAt());
        this.time = chatRoom.getTime();
    }
}
