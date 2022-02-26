package com.dalk.domain;

import com.dalk.Timestamped;
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
@Table(name = "item")
public class Item extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "bold", nullable = false)
    private Boolean bold = false;

    @Column(name = "color", nullable = false)
    private String color;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}