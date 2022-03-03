package com.dalk.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "bold", nullable = true)
    private Boolean bold = false;

    @Column(name = "color", nullable = true)
    private String color = "black";

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Item(boolean b, String black, User user1) {
        this.bold = b;
        this.color = black;
        this.user = user1;
    }
}