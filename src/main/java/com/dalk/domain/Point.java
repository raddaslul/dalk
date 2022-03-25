package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.dto.requestDto.GivePointRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.event.EventListener;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter

@Entity
@Table(name = "point")
public class Point extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String content;

    @Column
    private Long changePoint;

    @Column
    private Long totalPoint;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Point(String content, Long changePoint, User user) {
        this.content = content;
        this.changePoint = changePoint;
        this.totalPoint = user.getTotalPoint();
        this.user = user;
    }

//    public Point(GivePointRequestDto givePointRequestDto, Long totalPoint) {
//        this.content = givePointRequestDto.getContent();
//        this.changePoint = givePointRequestDto.getPoint();
//        this.totalPoint = totalPoint;
//        this.user = givePointRequestDto.getUsername();
//    }
}
