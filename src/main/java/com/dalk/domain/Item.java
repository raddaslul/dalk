package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
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

    @Column(name = "itemName")
    private String itemName;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Item(String itemName, Integer quantity,User user) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.user = user;
    }
    public Item(User user) {
        this.user = user;
    }


}