package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "point")
public class Point extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "changePoint", nullable = false)
    private Long changePoint;

    @Column(name = "resultPoint", nullable = false)
    private Long resultPoint;

    @JoinColumn(name = "userId")
    @ManyToOne(fetch =FetchType.LAZY )
    private User user;

    public Point(
            String content,
            Long changePoint,
            Long resultPoint,
            User user) {
        this.content = content;
        this.changePoint = changePoint;
        this.resultPoint = resultPoint;
        this.user = user;
    }
}
