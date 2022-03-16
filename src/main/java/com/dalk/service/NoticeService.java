package com.dalk.service;


import com.dalk.domain.Notice;
import com.dalk.dto.requestDto.NoticeRequestDto;
import com.dalk.dto.responseDto.NoticeResponseDto;
import com.dalk.exception.ex.NoticeNotFoundException;
import com.dalk.repository.NoticeRepository;
import com.dalk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;


    //공지글 생성 - 관리자만
    @Transactional
    public void createNotice(NoticeRequestDto noticeRequestDto) {
        Notice notice = new Notice(noticeRequestDto);
        noticeRepository.save(notice);

    }

    //공지글 전체 조회 - 로그인 안해도 볼수있음.
    public List<NoticeResponseDto> getNotices() {
        List<Notice> notices = noticeRepository.findAll();
        List<NoticeResponseDto> allNotices = new ArrayList<>();
        for (Notice notice : notices){
           NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice);
           allNotices.add(noticeResponseDto);
        }
        return allNotices;
    }

    //공지글 상세 조회 - 모든 유저
    public NoticeResponseDto getNoticesDetail(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                ()->new NoticeNotFoundException("해당 공지글이 없습니다.")
        );
        return new NoticeResponseDto(notice);
    }

    //공지글 수정 - 관리자만
    @Transactional
    public void updateNotices(Long noticeId, NoticeRequestDto noticeRequestDto) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                ()-> new NoticeNotFoundException("해당 공지글이 없습니다.")
        );
        notice.update(noticeRequestDto);
    }

    //공지글 삭제 - 관리자만
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                ()-> new NoticeNotFoundException("해당 공지글이 없습니다.")
        );
        noticeRepository.delete(notice);
    }


}
