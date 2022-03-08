package com.dalk.controller;

import com.dalk.domain.Carousel;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.CarouselResponseDto;
import com.dalk.service.AdminCarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminCarouselController {

    private final AdminCarouselService adminCarouselService;

    // 메인 배너 등록
    @Secured(User.Role.Authority.ADMIN)
    @PostMapping("/carousels")
    public HashMap<String, Object> createBanner(@RequestPart("image") MultipartFile multipartFile) throws IOException {
        Long carouselId = adminCarouselService.uploadFile(multipartFile);
        HashMap<String, Object> result = new HashMap<>();
        result.put("carouselId", carouselId);
        return result;
    }

    // 메인 배너 목록
    @Secured(User.Role.Authority.ADMIN)
    @GetMapping("/carousels")
    public List<CarouselResponseDto> getBanners() {
      return adminCarouselService.getBanners();
    }

    // 메인 배너 삭제
    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/carousels/{carouselId}")
    public HashMap<String, Object> deleteBanner(@PathVariable Long carouselId) {
        adminCarouselService.deleteBanner(carouselId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "true");
        return result;
    }
}
