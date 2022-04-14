package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.domain.vote.SaveVote;
import com.dalk.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class StaticService {

    public static RankRepository rankRepository;
    public static UserRepository userRepository;
    public static ItemRepository itemRepository;
    public static ChatMessageRepository chatMessageRepository;
    public static ChatRoomRepository chatRoomRepository;
    public static BoardRepository boardRepository;
    public static CommentRepository commentRepository;
    public static SaveVoteRepository saveVoteRepository;
    public static VoteRepository voteRepository;

    @Autowired
    public StaticService(ItemRepository itemRepository, RankRepository rankRepository, UserRepository userRepository,
                         ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository, BoardRepository boardRepository,
                         CommentRepository commentRepository, SaveVoteRepository saveVoteRepository, VoteRepository voteRepository) {
        StaticService.itemRepository = itemRepository;
        StaticService.rankRepository = rankRepository;
        StaticService.userRepository = userRepository;
        StaticService.chatMessageRepository = chatMessageRepository;
        StaticService.chatRoomRepository = chatRoomRepository;
        StaticService.boardRepository = boardRepository;
        StaticService.commentRepository = commentRepository;
        StaticService.saveVoteRepository = saveVoteRepository;
        StaticService.voteRepository = voteRepository;
    }

    public static Long changeItem(User user, ItemType itemType) {
        Item item = itemRepository.findByUser_IdAndItemCode(user.getId(), itemType.getItemCode());
        return item.getCnt();
    }

    public static void saveRank() {
        List<User> top3rankList = userRepository.findTop3ByOrderByExDescCreatedAtDesc();

        Ranking ranking1 = rankRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("현지훈메롱")
        );
        Ranking ranking2 = rankRepository.findById(2L).orElseThrow(
                () -> new IllegalArgumentException("김영민메롱")
        );
        Ranking ranking3 = rankRepository.findById(3L).orElseThrow(
                () -> new IllegalArgumentException("신동석메롱")
        );
        ranking1.setUserRank(top3rankList.get(0));
        ranking2.setUserRank(top3rankList.get(1));
        ranking3.setUserRank(top3rankList.get(2));
        rankRepository.save(ranking1);
        rankRepository.save(ranking2);
        rankRepository.save(ranking3);
    }

    public static Map<String, Object> deleteUserAllNull(Long userId) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByUser_Id(userId);
        for (ChatMessage chatMessage : chatMessageList) {
            if (chatMessage != null) {
                chatMessage.setUser(null);
                chatMessageRepository.save(chatMessage);
            }
        }

        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByUser_Id(userId);
        for (ChatRoom chatRoom : chatRoomList) {
            if (chatRoom != null) {
                chatRoom.setUser(null);
                chatRoomRepository.save(chatRoom);
            }
        }
        List<Board> boardList = boardRepository.findAllByUser_Id(userId);
        for (Board board : boardList) {
            if (board != null) {
                board.setUser(null);
                boardRepository.save(board);
            }
        }
        List<Comment> commentList = commentRepository.findAllByUser_Id(userId);
        for (Comment comment : commentList) {
            if (comment != null) {
                comment.setUser(null);
                commentRepository.save(comment);
            }
        }
        List<SaveVote> saveVoteList = saveVoteRepository.findAllByUser_Id(userId);
        for (SaveVote saveVote : saveVoteList) {
            if (saveVote != null) {
                saveVote.setUser(null);
            }
        }

        userRepository.deleteById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("result", true);
        return result;
    }
}

