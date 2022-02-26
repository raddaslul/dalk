package com.dalk.domain;

import com.dalk.domain.wl.WarnChatRoom;
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

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "time", nullable = false)
    private Boolean time = false;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true)
    private List<WarnChatRoom> warnChatRooms = new ArrayList<>();
}