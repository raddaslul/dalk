package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import com.dalk.domain.time.TimeConversion;
import com.dalk.domain.vote.SaveVote;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomEnterResponseDto {
    private UserInfoResponseDto userInfo;
    private UserVoteResponseDto userVoteResponseDto;
    private Long roomId;
    private String topicA;
    private String topicB;
    private String content;
    private List<String> category;
    private String filePath;
    private Long restTime;
    private String createdAt;
    private Boolean time;
    private Integer warnCnt;
    private Integer userCnt;
    private List<Long> warnUserList;

    public ChatRoomEnterResponseDto(SaveVote saveVote, ChatRoom chatRoom, List<String> categoryList, User user, Integer warnChatRoom, List<Long> warnUserList) {
        this.userInfo = new UserInfoResponseDto(user);
        this.userVoteResponseDto = new UserVoteResponseDto(saveVote);
        this.roomId = chatRoom.getId();
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
        this.category = categoryList;
        this.filePath = chatRoom.getFilePath();
        this.restTime = TimeConversion.restTime(chatRoom.getCreatedAt(),chatRoom.getTime());
        this.createdAt = TimeConversion.timeCreatedConversion(chatRoom.getCreatedAt());
        this.time = chatRoom.getTime();
        this.userCnt = chatRoom.getUserCnt();
        this.warnCnt=warnChatRoom;
        this.warnUserList=warnUserList;

}
    public ChatRoomEnterResponseDto(ChatRoom chatRoom, List<String> categoryList, User user, Integer warnChatRoom, List<Long> warnUserList) {
        this.userInfo = new UserInfoResponseDto(user);
        this.userVoteResponseDto = null;
        this.roomId = chatRoom.getId();
        this.topicA = chatRoom.getTopicA();
        this.topicB = chatRoom.getTopicB();
        this.category = categoryList;
        this.filePath = chatRoom.getFilePath();
        this.restTime = TimeConversion.restTime(chatRoom.getCreatedAt(),chatRoom.getTime());
        this.createdAt = TimeConversion.timeCreatedConversion(chatRoom.getCreatedAt());
        this.time = chatRoom.getTime();
        this.userCnt = chatRoom.getUserCnt();
        this.warnCnt=warnChatRoom;
        this.warnUserList=warnUserList;
    }
}
