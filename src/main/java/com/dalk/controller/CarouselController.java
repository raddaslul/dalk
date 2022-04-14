package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.domain.UserRole;
import com.dalk.dto.requestDto.CarouselRequestDto;
import com.dalk.dto.responseDto.CarouselResponseDto;
import com.dalk.service.CarouselService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CarouselController {

    private final CarouselService carouselService;

    @Secured(UserRole.Authority.ADMIN)
    @PostMapping("/admin/carousels")
    @ApiOperation(value = "Admin에서 배너 등록")
    public Map<String, Object> createBanner(
            @RequestPart("image") MultipartFile multipartFile,
            @RequestPart(value = "url", required = false) CarouselRequestDto carouselRequestDto) throws IOException {
        Long carouselId = carouselService.uploadFile(multipartFile, carouselRequestDto);
        Map<String, Object> result = new HashMap<>();
        result.put("carouselId", carouselId);
        return result;
    }

    @Secured(UserRole.Authority.ADMIN)
    @GetMapping("/admin/carousels")
    @ApiOperation(value = "Admin에서 배너 목록 조회")
    public List<CarouselResponseDto> getAdminBanners() {
        return carouselService.getBanners();
    }

    @Secured(UserRole.Authority.ADMIN)
    @DeleteMapping("/admin/carousels/{carouselId}")
    @ApiOperation(value = "Admin에서 배너 삭제")
    public Map<String, Object> deleteBanner(@PathVariable Long carouselId) {
        return carouselService.deleteBanner(carouselId);
    }

    @GetMapping("/api/carousels")
    @ApiOperation(value = "메인페이지에서 배너 조회")
    public List<CarouselResponseDto> getMainBanners() {
        return carouselService.getBanners();
    }
}