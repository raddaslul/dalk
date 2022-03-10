package com.dalk.domain;

import com.dalk.domain.time.Timestamped;
import com.dalk.domain.vote.SaveVote;
import com.dalk.domain.wl.WarnChatRoom;
import com.dalk.dto.requestDto.ChatRoomRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<Category> categorys;

    @Column(name = "time", nullable = false)
    private Boolean time;

    @Column(nullable = false)
    private Long createUserId;

    @Column(nullable = false)
    private Boolean status;

    @Column
    private String convertedFileName;

    @Column
    private String filePath;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true)
    private List<ChatRoomUser> chatRoomUser;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<SaveVote> saveVoteList;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true)
    private List<WarnChatRoom> warnChatRooms = new ArrayList<>();

    public ChatRoom(ChatRoomRequestDto requestDto, Long userId, String convertedFileName, String filePath) {
        this.topicA = requestDto.getTopicA();
        this.topicB = requestDto.getTopicB();
        this.time = requestDto.getTime();
        this.createUserId = userId;
        this.status = requestDto.getStatus();
        this.convertedFileName = convertedFileName;
        this.filePath = filePath;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}