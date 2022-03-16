package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(indexes = @Index(name = "user", columnList = "id"))
public class User extends Timestamped {

    public enum Role {
        USER(Authority.USER),  // 사용자 권한
        ADMIN(Authority.ADMIN);  // 관리자 권한

        private final String authority;

        Role(String authority) {
            this.authority = authority;
        }

        public String getAuthority() {
            return this.authority;
        }

        public static class Authority {

            public static final String USER = "ROLE_USER";
            public static final String ADMIN = "ROLE_ADMIN";
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "totalpoint")
    private Long totalPoint;

    @Column(name = "ex")
    private Integer ex;

    @Column(name = "warnUser")
    private Integer warnUserCnt;

    @Column(name = "rank")
    private Integer rank;

    @Column
    @Enumerated(value = EnumType.STRING) // 정보를 받을 때는 Enum 값으로 받지만
    // db에 갈때는 Spring Jpa에 의해 자동으로 String으로 변환됨
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private ChatRoomUser chatRoomUser;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Item> items;

    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
    private Lotto lotto;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Point> points;

    public void setTotalPoint(Long totalPoint) {
        this.totalPoint = totalPoint;
    }

    public void setEx(Integer ex) {
        this.ex = ex;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setWarnUserCnt(Integer warnUserCnt){this.warnUserCnt =warnUserCnt;}

    public User(String username, String password, String nickname, Long totalPoint, Integer ex,Integer warnUserCnt, Role role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.totalPoint = totalPoint;
        this.ex = ex;
        this.warnUserCnt=warnUserCnt;
        this.role = role;
    }
}