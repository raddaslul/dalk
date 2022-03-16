package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column
    private String itemCode;

    @Column
    private Long cnt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }

    public Item(String itemCode, Long cnt, User user) {
        this.itemCode = itemCode;
        this.cnt = cnt;
        this.user = user;
    }
}
