package com.dalk.dto.responseDto;


import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PointsResponseDto {
    private Long id;
    private Long changepoints;
    private Long totalpoints;
    private String status;



}
