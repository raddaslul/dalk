package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.requestDto.NoticeRequestDto;
import com.dalk.dto.responseDto.NoticeResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.BoardService;
import com.dalk.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class noticeController {


    private final NoticeService noticeService;


    //관리자만 공지글 작성
    @Secured(User.Role.Authority.ADMIN)
    @PostMapping("/admin/notices")
    public void createNotice( NoticeRequestDto noticeRequestDto){
        noticeService.createNotice(noticeRequestDto);
    }

    //전체공지글 조회
    @GetMapping("/api/notices")
    public List<NoticeResponseDto> getNotices(){
        return noticeService.getNotices();
    }

    //공지글 상세 페이지
    @GetMapping("/api/notices/{noticeId}")
    public NoticeResponseDto getNoticesDetail(@PathVariable Long noticeId){
        return noticeService.getNoticesDetail(noticeId);
    }

    //공지글 수정
    @Secured(User.Role.Authority.ADMIN)
    @PutMapping("/admin/notices/{noticeId}")
    public Long updateNotices(@PathVariable Long noticeId, @RequestBody NoticeRequestDto noticeRequestDto) {
        noticeService.updateNotices(noticeId, noticeRequestDto);
        return noticeId;
    }

    //공지글 삭제
    @Secured(User.Role.Authority.ADMIN)
    @DeleteMapping("/admin/notices/{noticeId}")
    public void deleteNotice(@PathVariable Long noticeId){
        noticeService.deleteNotice(noticeId);
    }


}
