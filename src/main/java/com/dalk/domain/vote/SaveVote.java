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
@Table(name = "save_vote")
public class SaveVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Boolean pick;

    @Column
    private Long point;

    @ManyToOne
    @JoinColumn(name = "ChatRoomId")
    private ChatRoom chatRoom;

    @ManyToOne
    private Vote vote;

    @ManyToOne
    private User user;

    public SaveVote(Boolean pick, ChatRoom chatRoom, Long point, Vote vote, User user) {
        this.pick = pick;
        this.point = point;
        this.chatRoom = chatRoom;
        this.vote = vote;
        this.user = user;
    }
}
