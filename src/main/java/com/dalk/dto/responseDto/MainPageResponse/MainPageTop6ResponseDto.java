package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.dto.responseDto.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainPageTop6ResponseDto {
    private UserInfoResponseDto userInfo;
    private Long chatRoomId;
    private String topicA;
    private String topicB;
    private String content;
    private String category;
    private String restTime;
    private LocalDateTime createdAt;

//    private LocalDateTime now = LocalDateTime.now();


}
