package com.dalk.service;

import com.dalk.domain.Carousel;
import com.dalk.dto.responseDto.CarouselResponseDto;
import com.dalk.exception.ex.CarouselNotFoundException;
import com.dalk.repository.CarouselRepository;
import com.dalk.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminCarouselService {

    private final S3Repository s3Repository;
    private final CarouselRepository carouselRepository;

    // 메인 배너 등록
    public Long uploadFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String convertedFileName = UUID.randomUUID() + originalFileName;
        String filePath = s3Repository.upload(multipartFile, convertedFileName);
        Carousel carousel = new Carousel(convertedFileName, filePath);
        carouselRepository.save(carousel);
        return carousel.getId();
    }

    // 메인 배너 조회
    public List<CarouselResponseDto> getBanners() {
        List<CarouselResponseDto> carouselResponseDtoList = new ArrayList<>();
        List<Carousel> carouselList = carouselRepository.findAll();
        for (Carousel carousel : carouselList) {
            CarouselResponseDto carouselResponseDto = new CarouselResponseDto(carousel);
            carouselResponseDtoList.add(carouselResponseDto);
        }
        return carouselResponseDtoList;
    }

    // 메인 배너 삭제
    public void deleteBanner(Long carouselId) {
        Carousel carousel = carouselRepository.findById(carouselId)
                .orElseThrow(() -> new CarouselNotFoundException("해당 배너가 존재하지 않습니다."));
        String deleteFileURL = "image/" + carousel.getConvertedName();
        s3Repository.deleteFile(deleteFileURL);
        carouselRepository.delete(carousel);
    }
}
