package com.dalk.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {
    @CreatedDate // 최초 생성 시점
    private LocalDate createdAt;

    @LastModifiedDate // 마지막 변경 시점
    private LocalDate modifiedAt;

    @CreatedDate
    private LocalDateTime createdAtTime;
}