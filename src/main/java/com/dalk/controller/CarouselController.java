package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.responseDto.CarouselResponseDto;
import com.dalk.service.CarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarouselController {

    private final CarouselService carouselService;

    // Admin에서 배너 등록
    @Secured(User.Role.Authority.ADMIN)
    @PostMapping("/admin/carousels")
    public HashMap<String, Object> createBanner(@RequestPart("image") MultipartFile multipartFile) throws IOException {
        Long carouselId = carouselService.uploadFile(multipartFile);
        HashMap<String, Object> result = new HashMap<>();
        result.put("carouselId", carouselId);
        return result;
    }

    // Admin에서 배너 목록 조회
    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/admin/carousels")
    public List<CarouselResponseDto> getAdminBanners() {
      return carouselService.getBanners();
    }

    // Admin에서 배너 삭제
    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/admin/carousels/{carouselId}")
    public HashMap<String, Object> deleteBanner(@PathVariable Long carouselId) {
        carouselService.deleteBanner(carouselId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }

    // 메인페이지에서 배너 조회
    @GetMapping("/api/carousels")
    public List<CarouselResponseDto> getMainBanners() {
        return carouselService.getBanners();
    }
}
