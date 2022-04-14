package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.domain.UserRole;
import com.dalk.dto.requestDto.NoticeRequestDto;
import com.dalk.dto.responseDto.NoticeResponseDto;
import com.dalk.service.NoticeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class noticeController {

    private final NoticeService noticeService;

    @Secured(UserRole.Authority.ADMIN)
    @PostMapping("/admin/notices")
    @ApiOperation(value = "공지글 작성")
    public Map<String, Object> createNotice(@RequestBody NoticeRequestDto noticeRequestDto) {
        return noticeService.createNotice(noticeRequestDto);
    }

    @GetMapping("/api/notices")
    @ApiOperation(value = "공지글 조회")
    public List<NoticeResponseDto> getNotices() {
        return noticeService.getNotices();
    }

    @GetMapping("/api/notices/{noticeId}")
    @ApiOperation(value = "공지글 상세페이지 조회")
    public NoticeResponseDto getNoticesDetail(@PathVariable Long noticeId) {
        return noticeService.getNoticesDetail(noticeId);
    }

    @Secured(UserRole.Authority.ADMIN)
    @PutMapping("/admin/notices/{noticeId}")
    @ApiOperation(value = "공지글 수정")
    public Map<String, Object> updateNotices(@PathVariable Long noticeId, @RequestBody NoticeRequestDto noticeRequestDto) {
        return noticeService.updateNotices(noticeId, noticeRequestDto);
    }

    @Secured(UserRole.Authority.ADMIN)
    @DeleteMapping("/admin/notices/{noticeId}")
    @ApiOperation(value = "공지글 삭제")
    public Map<String, Object> deleteNotice(@PathVariable Long noticeId) {
        return noticeService.deleteNotice(noticeId);
    }
}
