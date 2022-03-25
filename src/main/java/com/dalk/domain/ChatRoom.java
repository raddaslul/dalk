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

    @Column(name = "topic_a", nullable = false, length = 14)
    private String topicA;

    @Column(name = "topic_b", nullable = false, length = 14)
    private String topicB;

    @Column(name = "time", nullable = false)
    private Boolean time;

    @Column(nullable = false)
    private Long createUserId;

    @Column
    private String convertedFileName;

    @Column
    private String filePath;

    @Column
    private Integer userCnt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<Category> categorys;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatRoomUser> chatRoomUser;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<SaveVote> saveVoteList;

    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true)
    private List<WarnChatRoom> warnChatRooms = new ArrayList<>();

    public void setUserCnt(Integer userCnt) {
        this.userCnt = userCnt;
    }

    public ChatRoom(ChatRoomRequestDto requestDto, Long userId, String convertedFileName, String filePath) {
        this.topicA = requestDto.getTopicA();
        this.topicB = requestDto.getTopicB();
        this.time = requestDto.getTime();
        this.createUserId = userId;
        this.convertedFileName = convertedFileName;
        this.filePath = filePath;
        this.userCnt = 0;
    }

    public ChatRoom(String topicA, String topicB,List<Category> categorys,Boolean time,Long createUserId,Boolean status ) {
        this.topicA = topicA;
        this.topicB = topicB;
        this.time = time;
        this.categorys= categorys;
        this.createUserId = createUserId;
        this.userCnt = 0;
    }
}