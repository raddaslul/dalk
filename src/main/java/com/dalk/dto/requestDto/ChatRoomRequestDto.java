package com.dalk.dto.requestDto;

import lombok.Data;

import java.util.List;

@Data
public class ChatRoomRequestDto {
    private String topicA;
    private String topicB;
    private String content;
    private List<String> category;
    private Boolean time;
}
