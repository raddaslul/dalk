package com.dalk.dto.requestDto.MainPageRequest;

import lombok.Data;

@Data
public class CreateChatRoomRequestDto {
    private String topicA;
    private String topicB;
    private String content;
    private String category;
    private Boolean time;
}
