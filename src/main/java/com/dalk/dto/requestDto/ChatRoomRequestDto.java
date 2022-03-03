package com.dalk.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequestDto {

    private String topicA;
    private String topicB;
    private String content;
    private String category;
    private Boolean time;
    private Long userId;
}