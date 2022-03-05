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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String content;

    @Column
    private Long changePoint;

    @Column
    private Long toTalPoint;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Point(String content, Long changePoint, Long toTalPoint, User user) {
        this.content = content;
        this.changePoint = changePoint;
        this.toTalPoint = toTalPoint;
        this.user = user;
    }
}
