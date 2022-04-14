package com.dalk.domain.vote;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class SaveVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean pick;

    @Column
    private Long point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChatRoomId")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote")
    private Vote vote;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public SaveVote(Boolean pick, ChatRoom chatRoom, Long point, Vote vote, User user) {
        this.pick = pick;
        this.point = point;
        this.chatRoom = chatRoom;
        this.vote = vote;
        this.user = user;
    }
}
