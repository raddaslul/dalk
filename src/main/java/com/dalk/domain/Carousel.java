package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.dto.requestDto.CarouselRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Carousel extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "converted_name", nullable = false)
    private String convertedName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private Boolean status;

    public Carousel(String convertedName, String filePath, CarouselRequestDto carouselRequestDto) {
        this.filePath = filePath;
        this.convertedName = convertedName;
        if (carouselRequestDto != null) {
            this.url = carouselRequestDto.getUrl();
            this.status = carouselRequestDto.getStatus();
        } else {
            this.url = null;
            this.status = false;
        }
    }
}