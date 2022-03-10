package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import lombok.*;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "bigFont")
    private Integer bigFont;

    @Column(name = "onlyMe")
    private Integer onlyMe;

    @Column(name = "myName")
    private Integer myName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public Item(Integer bigFont, Integer onlyMe, Integer myName) {
        this.bigFont = bigFont;
        this.onlyMe = onlyMe;
        this.myName = myName;
    }

    public void setBigFont(Integer bigFont) {
        this.bigFont = bigFont;
    }

    public void setOnlyMe(Integer onlyMe) {
        this.onlyMe = onlyMe;
    }

    public void setMyName(Integer myName) {
        this.myName = myName;
    }
}