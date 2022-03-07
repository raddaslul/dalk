package com.dalk.domain.vote;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "save_vote")
public class SaveVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Boolean pick;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private Vote vote;

    @ManyToOne
    private User user;

    public SaveVote(Boolean pick, ChatRoom chatRoom, Vote vote, User user) {
        this.pick = pick;
        this.chatRoom = chatRoom;
        this.vote = vote;
        this.user = user;
    }
}
