package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

    public Item(String itemCode, Long cnt, User user) {
        this.itemCode = itemCode;
        this.cnt = cnt;
        this.user = user;
    }
}
