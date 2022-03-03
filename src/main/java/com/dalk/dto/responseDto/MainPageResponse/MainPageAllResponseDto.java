package com.dalk.dto.responseDto.MainPageResponse;

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
}
