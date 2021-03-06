package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String itemCode;

    @Column
    private Long cnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Item(String itemCode, Long cnt, User user) {
        this.itemCode = itemCode;
        this.cnt = cnt;
        this.user = user;
    }

    public void itemAdd() {
        this.cnt += 1;
    }

    public void itemSubtract() {
        this.cnt -= 1;
    }
}
