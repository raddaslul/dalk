package com.dalk.repository;

import com.dalk.domain.Notice;
import com.dalk.dto.requestDto.NoticeRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
