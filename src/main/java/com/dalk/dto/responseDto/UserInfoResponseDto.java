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
public class UserInfoResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private Long point;
    private Integer level;
    private User.Role role ;
    private ItemResponseDto item;



    public UserInfoResponseDto(User user) {
        ItemResponseDto itemResponseDto= new ItemResponseDto(user);
        PointsResponseDto pointsResponseDto =new PointsResponseDto(user);
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.point = pointsResponseDto.getPoints();
        this.level = user.getLevel();
        this.role = user.getRole();
        this.item = itemResponseDto;
    }


    public UserInfoResponseDto(Long id, String nickname, Integer level) {
        this.id = id;
        this.nickname = nickname;
        this.level = level;
    }
}
