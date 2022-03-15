package com.dalk.domain;


import com.dalk.domain.time.Timestamped;
import com.dalk.dto.requestDto.NoticeRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(indexes= @Index(name = "notice", columnList = "id"))
public class Notice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title" )
    private String title;

    @Column(name = "content")
    private String content;

//    @ManyToOne
//    private User user;

    public Notice(NoticeRequestDto noticeRequestDto) {
       this.title = noticeRequestDto.getTitle();
       this.content = noticeRequestDto.getContent();
    }
    public void update(NoticeRequestDto noticeRequestDto) {
        this.title = noticeRequestDto.getTitle();
        this.content = noticeRequestDto.getContent();
    }
}
