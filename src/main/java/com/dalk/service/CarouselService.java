package com.dalk.service;

import com.dalk.domain.Carousel;
import com.dalk.dto.requestDto.CarouselRequestDto;
import com.dalk.dto.responseDto.CarouselResponseDto;
import com.dalk.exception.ex.CarouselNotFoundException;
import com.dalk.repository.CarouselRepository;
import com.dalk.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CarouselService {

    private final S3Repository s3Repository;
    private final CarouselRepository carouselRepository;

    // 메인 배너 등록
    @Transactional
    public Long uploadFile(MultipartFile multipartFile, CarouselRequestDto carouselRequestDto) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String convertedFileName = UUID.randomUUID() + originalFileName;
        String filePath = s3Repository.upload(multipartFile, convertedFileName);
        Carousel carousel = new Carousel(convertedFileName, filePath, carouselRequestDto);
        carouselRepository.save(carousel);
        return carousel.getId();
    }

    // 메인 배너 조회
    @Transactional(readOnly = true)
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
    @Transactional
    public Map<String, Object> deleteBanner(Long carouselId) {
        Carousel carousel = carouselRepository.findById(carouselId)
                .orElseThrow(() -> new CarouselNotFoundException("해당 배너가 존재하지 않습니다."));
        String deleteFileURL = "image/" + carousel.getConvertedName();
        s3Repository.deleteFile(deleteFileURL);
        carouselRepository.delete(carousel);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }
}