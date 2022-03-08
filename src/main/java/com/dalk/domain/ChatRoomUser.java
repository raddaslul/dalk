package com.dalk.domain;

//import com.dalk.domain.vote.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "chat_room_user")
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

//    @ManyToOne
//    @JoinColumn(name = "vote")
//    private Vote vote;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
}
