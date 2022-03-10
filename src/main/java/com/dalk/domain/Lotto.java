package com.dalk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long count;

    @OneToOne
    private User user;

    public void setCount(Long count) {
        this.count = count;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lotto(Long count, User user) {
        this.count = count;
        this.user = user;
    }
}
