package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import com.dalk.domain.time.TimeConversion;
import com.dalk.dto.responseDto.CreatorInfoResponseDto;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MainPageAllResponseDto {
    private CreatorInfoResponseDto userInfo;
    private Long roomId;
    private String filePath;
    private String topicA;
    private String topicB;
    private String content;
    private List<String> category;
    private String createdAt;
    private String endAt;
    private Boolean time;
    private Integer warnCnt;
    private Integer userCnt;
    private List<Long> warnUserList;

    public MainPageAllResponseDto(ChatRoom chatRoom, List<String> categoryList,Integer warnChatRoom,List<Long> warnUserList) {
        if (chatRoom.getUser() == null) {
            this.userInfo = new CreatorInfoResponseDto(null);
        } else {
            this.userInfo = new CreatorInfoResponseDto(chatRoom.getUser());
        }
        this.roomId = chatRoom.getId();
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
        this.category = categoryList;
        this.filePath = chatRoom.getFilePath();
        this.createdAt = TimeConversion.timeCreatedConversion(chatRoom.getCreatedAt());
        this.endAt = TimeConversion.timeEndConversion(chatRoom);
        this.time = chatRoom.getTime();
        this.userCnt = chatRoom.getChatRoomUser().size();
        this.warnCnt = warnChatRoom;
        this.warnUserList = warnUserList;
    }
}
