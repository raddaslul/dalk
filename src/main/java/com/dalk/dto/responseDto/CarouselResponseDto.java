package com.dalk.dto.responseDto;

import com.dalk.domain.Carousel;
import lombok.Data;

@Data
public class CarouselResponseDto {
    private Long carouselId;
    private String image;
    private String url;
    private Boolean status;

    public CarouselResponseDto(Carousel carousel){
        this.carouselId = carousel.getId();
        this.image = carousel.getFilePath();
        this.url = carousel.getUrl();
        this.status = carousel.getStatus();
    }
}
