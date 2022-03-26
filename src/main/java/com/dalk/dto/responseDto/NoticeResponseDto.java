package com.dalk.dto.responseDto;

import com.dalk.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoticeResponseDto{

    private Long NoticeId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public NoticeResponseDto(Notice notice) {
        this.NoticeId = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdAt =notice.getCreatedAt();
        this.modifiedAt = notice.getModifiedAt();
    }
}
