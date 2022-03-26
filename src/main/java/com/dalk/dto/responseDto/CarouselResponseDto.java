package com.dalk.dto.responseDto;

import com.dalk.domain.Carousel;
import lombok.Data;

@Data
public class CarouselResponseDto {
    private Long CarouselId;
    private String image;
    private String url;

    public CarouselResponseDto(Carousel carousel){
        this.CarouselId = carousel.getId();
        this.image = carousel.getFilePath();
        this.url = carousel.getUrl();
    }
}
