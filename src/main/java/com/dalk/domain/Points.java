package com.dalk.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "points")
public class Points extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "change" , nullable = true)
    private Long changepoint = 0L;

    @Column(name = "totalpoints", nullable = true)
    private Long totalpoints = 0L;

    @Column(name = "status", nullable = true)
    private String content;


    //findtop1byorderbycreatedatdesc 가장최근포인트를 불러온다.
//    4개를 불러오는 수식
//    거기서 가장최근포인트 recentpoint라두면 , long rescent [ptint =get recnetpoint .
    @ManyToOne
    @JoinColumn
    private User user;

    public Points(long changepoint,long totalpoints, String content, User user) {
        this.changepoint=changepoint;
        this.totalpoints=totalpoints;
        this.content = content;
        this.user = user;
    }



}
