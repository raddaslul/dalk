package com.dalk.dto.requestDto.MainPageRequest;

import lombok.Data;

import java.util.List;

@Data
public class CreateChatRoomRequestDto {
    private String topicA;
    private String topicB;
    private String content;
    private List<String> category;
    private Boolean time;
}
