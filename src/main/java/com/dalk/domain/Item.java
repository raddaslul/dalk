package com.dalk.domain;

import com.dalk.dto.responseDto.ItemResponseDto;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "bold", nullable = false)
    private Boolean bold;

    @Column(name = "color", nullable = false)
    private String color;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}