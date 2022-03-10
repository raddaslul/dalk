package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import com.dalk.domain.time.TimeConversion;
import com.dalk.dto.responseDto.UserInfoResponseDto;
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
    private List<String> category;
    private Long restTime;
    private String createdAt;
    private Boolean time;
    private Integer warnCnt;
    private List<Long> warnUserList;

    public MainPageAllResponseDto(ChatRoom chatRoom, List<String> categoryList, User user,Integer warnChatRoom,List<Long> warnUserList) {
        this.userInfo = new UserInfoResponseDto(user);
        this.roomId = chatRoom.getId();
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
        this.category = categoryList;
        this.restTime = TimeConversion.restTime(chatRoom.getCreatedAt(),chatRoom.getTime());
        this.createdAt = TimeConversion.timeCreatedConversion(chatRoom.getCreatedAt());
        this.time = chatRoom.getTime();
        this.warnCnt=warnChatRoom;
        this.warnUserList=warnUserList;
    }
}
