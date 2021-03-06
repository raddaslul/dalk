package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.domain.wl.*;
import com.dalk.exception.ex.ItemNotFoundException;
import com.dalk.exception.ex.LackPointException;
import com.dalk.service.StaticService;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
//@Table(indexes = @Index(name = "user", columnList = "id"))
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "lottoCount")
    private Integer lottoCnt;

    @Column
    @Enumerated(value = EnumType.STRING) // 정보를 받을 때는 Enum 값으로 받지만
    // db에 갈때는 Spring Jpa에 의해 자동으로 String으로 변환됨
    private UserRole role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> boardList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatRoom> chatRoomList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatMessage> chatMessageList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Item> items;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Point> points;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Ranking ranking;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Agree> agreeList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<WarnBoard> warnBoardList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<WarnChatRoom> warnChatRoomList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<WarnComment> warnCommentList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<WarnUser> warnUserList;

    public void setEx(Integer ex) {
        this.ex = ex;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public User(String username, String password, String nickname, Long totalPoint, Integer ex, UserRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.totalPoint = totalPoint;
        this.ex = ex;
        this.role = role;
        this.ranking = null;
        this.lottoCnt = 5;
    }

    public void buyItem(ItemType item, Item userItem) {
        if (this.totalPoint < item.getPrice()) {
            throw new LackPointException
                    ("보유한 포인트가 부족합니다");
        }
        this.totalPoint -= item.getPrice();
        if (item.getItemCode().equals("exBuy")) {
            this.ex += item.getPrice().intValue();
            StaticService.saveRank();
        } else {
            userItem.itemAdd();
        }
    }

    public void useItem(Item userItem) {
        if (userItem.getCnt() > 0) {
            userItem.itemSubtract();
        } else {
            throw new ItemNotFoundException
                    ("아이템이 없습니다");
        }
    }

    public void refreshCount() {
        this.lottoCnt = 5;
    }

    public void subtractCount() {
        this.lottoCnt -= 1;
    }

    public void totalPointAdd(Long totalPoint) {
        this.totalPoint += totalPoint;
    }

    public void totalPointSubtract(Long point) {
        if (this.totalPoint < point) {
            throw new LackPointException("보유한 포인트가 부족합니다");
        }
        this.totalPoint = this.totalPoint - point;
    }
}