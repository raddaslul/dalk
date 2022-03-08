package com.dalk.dto.responseDto;


import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.domain.time.TimeConversion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PointResponseDto {

    private String content;
    private Long changePoint;
    private Long totalPoint;
    private String createdAt;

    public PointResponseDto(Point point){
        this.content =point.getContent();
        this.changePoint = point.getChangePoint();
        this.totalPoint = point.getToTalPoint();
        this.createdAt = TimeConversion.timeCreatedConversion(point.getCreatedAt());
    }
}
