package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.requestDto.NoticeRequestDto;
import com.dalk.dto.responseDto.NoticeResponseDto;
import com.dalk.service.NoticeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class noticeController {

    private final NoticeService noticeService;

    @Secured(User.Role.Authority.ADMIN)
    @PostMapping("/admin/notices")
    @ApiOperation(value = "공지글 작성")
    public void createNotice(@RequestBody NoticeRequestDto noticeRequestDto){

        noticeService.createNotice(noticeRequestDto);
    }
    @GetMapping("/api/notices")
    @ApiOperation(value = "공지글 조회")
    public List<NoticeResponseDto> getNotices(){
        return noticeService.getNotices();
    }

    @GetMapping("/api/notices/{noticeId}")
    @ApiOperation(value = "공지글 상세페이지 조회")
    public NoticeResponseDto getNoticesDetail(@PathVariable Long noticeId){
        return noticeService.getNoticesDetail(noticeId);
    }

    @Secured(User.Role.Authority.ADMIN)
    @PutMapping("/admin/notices/{noticeId}")
    @ApiOperation(value = "공지글 수정")
    public void updateNotices(@PathVariable Long noticeId, @RequestBody NoticeRequestDto noticeRequestDto) {
        noticeService.updateNotices(noticeId,noticeRequestDto);
    }

    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/admin/notices/{noticeId}")
    @ApiOperation(value = "공지글 삭제")
    public void deleteNotice(@PathVariable Long noticeId){
        noticeService.deleteNotice(noticeId);
    }


}
