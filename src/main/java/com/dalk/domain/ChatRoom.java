package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.requestDto.ChatRoomRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "chat_room")
public class ChatRoom extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "topic_a", nullable = false)
    private String topicA;

    @Column(name = "topic_b", nullable = false)
    private String topicB;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<Category> categorys = new ArrayList<>();

    @Column(name = "time", nullable = false)
    private Boolean time;

    @Column(nullable = false)
    private Long createUserId;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true)
    private List<ChatRoomUser> chatRoomUser;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true)
    private List<WarnChatRoom> warnChatRooms = new ArrayList<>();

    public ChatRoom(ChatRoomRequestDto requestDto, Long userId) {
        this.topicA = requestDto.getTopicA();
        this.topicB = requestDto.getTopicB();
        this.time = requestDto.getTime();
        this.createUserId = userId;
    }
}