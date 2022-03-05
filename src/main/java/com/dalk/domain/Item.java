package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "item")
public class Item extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
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