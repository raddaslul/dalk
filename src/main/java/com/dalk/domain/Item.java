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

    @Column(name = "bigFont")
    private Integer bigFont;

    @Column(name = "onlyMe")
    private Integer onlyMe;

    @Column(name = "myName")
    private Integer myName;

    //    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
    public Item(Integer bigFont, Integer onlyMe, Integer myName) {
        this.bigFont = bigFont;
        this.onlyMe = onlyMe;
        this.myName = myName;
    }
}