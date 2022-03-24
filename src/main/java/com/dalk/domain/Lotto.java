package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "lotto")
public class Lotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private Integer count;

    @OneToOne
    private User user;


    public void refreshCount() {
        this.count =0;
    }

    public void addCount() {
        this.count += 1;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lotto(Integer count, User user) {
        this.count = count;
        this.user = user;
    }
}
