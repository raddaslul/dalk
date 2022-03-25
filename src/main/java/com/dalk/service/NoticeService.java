package com.dalk.service;

import com.dalk.domain.Notice;
import com.dalk.dto.requestDto.NoticeRequestDto;
import com.dalk.dto.responseDto.NoticeResponseDto;
import com.dalk.exception.ex.NoticeNotFoundException;
import com.dalk.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    //공지글 생성 - 관리자만
    @Transactional
    public Map<String, Object> createNotice(NoticeRequestDto noticeRequestDto) {
        Notice notice = new Notice(noticeRequestDto);
        noticeRepository.save(notice);
        Map<String, Object> result = new HashMap<>();
        result.put("noticeId", notice.getId());
        return result;
    }

    //공지글 전체 조회 - 로그인 안해도 볼수있음.
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public NoticeResponseDto getNoticesDetail(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                ()->new NoticeNotFoundException("해당 공지글이 없습니다.")
        );
        return new NoticeResponseDto(notice);
    }

    //공지글 수정 - 관리자만
    @Transactional
    public Map<String, Object> updateNotices(Long noticeId, NoticeRequestDto noticeRequestDto) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                ()-> new NoticeNotFoundException("해당 공지글이 없습니다.")
        );
        notice.update(noticeRequestDto);

        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }

    //공지글 삭제 - 관리자만
    @Transactional
    public Map<String, Object> deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(
                ()-> new NoticeNotFoundException("해당 공지글이 없습니다.")
        );
        noticeRepository.delete(notice);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }
}
