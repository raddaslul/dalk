package com.dalk.domain.wl;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.User;
import com.dalk.domain.time.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "warn_chat_room")
public class WarnChatRoom extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_warn")
    private Boolean isWarn ;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public WarnChatRoom(boolean isWarn, ChatRoom chatRoom, User user) {
        this.isWarn = isWarn;
        this.chatRoom = chatRoom;
        this.user = user;
    }
}