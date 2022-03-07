package com.dalk.service;

import com.dalk.domain.ChatRoom;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.domain.vote.SaveVote;
import com.dalk.domain.vote.Vote;
import com.dalk.dto.requestDto.VoteRequestDto;
import com.dalk.dto.responseDto.VoteUserListResponseDto;
import com.dalk.exception.ex.ChatRoomNotFoundException;
import com.dalk.exception.ex.DuplicateVoteException;
import com.dalk.exception.ex.LackPointException;
import com.dalk.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {
    private final ChatRoomRepository chatRoomRepository;
    private final VoteRepository voteRepository;
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final SaveVoteRepository saveVoteRepository;

    //투표 버튼 누를때마다
    //예외처리 안해놈 => 같은사람이 투표 한번 더하면 오류
    @Transactional
    public void saveVote(Long roomId, User user, VoteRequestDto requestDto) {
        Vote vote = voteRepository.findByChatRoom_Id(roomId); //투표 찾아서
        SaveVote savevote = saveVoteRepository.findByUser_IdAndChatRoom_IdAndVote_Id(user.getId(),roomId,vote.getId());
        if (savevote == null) {
            if(user.getTotalPoint() > requestDto.getPoint())
            {
                ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                        () -> new ChatRoomNotFoundException("해당 채팅방이 없습니다")
                );
                user.setTotalPoint(user.getTotalPoint()-requestDto.getPoint()); //유저의 포인트 변화
                userRepository.save(user); //저장
                Point point = new Point("투표 참여", -requestDto.getPoint(), user.getTotalPoint(), user); //포인트 내역 생성
                pointRepository.save(point); //포인트 내역 저장

                if (requestDto.getTopic()) { // topicA를 골랐을 때
                    SaveVote saveVote = new SaveVote(true, chatRoom, vote, user); // savevote 추가
                    saveVoteRepository.save(saveVote); //저장
                    vote.setTotalPointA(vote.getTotalPointA()+requestDto.getPoint()); //totalpoint 추가
                    vote.setTopicACnt(vote.getTopicACnt()+1);
                    if (vote.getTopPointA() < requestDto.getPoint()) { //toppoint찾기
                        vote.setTopPointA(requestDto.getPoint());
                    }

                    voteRepository.save(vote);
                } else {//topicB를 골랐을 때
                    SaveVote saveVote = new SaveVote(false, chatRoom, vote, user);
                    saveVoteRepository.save(saveVote); //저장
                    vote.setTotalPointB(vote.getTotalPointB()+requestDto.getPoint()); //totalpoint 추가
                    vote.setTopicBCnt(vote.getTopicBCnt()+1);
                    if (vote.getTopPointB() < requestDto.getPoint()) { //toppoint찾기
                        vote.setTopPointB(requestDto.getPoint());
                    }
                    voteRepository.save(vote);
                }
            }else throw new LackPointException("보유한 포인트가 부족합니다");
        }else throw new DuplicateVoteException("이미 투표에 참여하셨습니다");
    }

    //토론방 유저 리스트
    public List<VoteUserListResponseDto> voteUserList(Long roomId) {
        List<VoteUserListResponseDto> voteUserListResponseDtoList = new ArrayList<>();
        List<SaveVote> saveVotesList = saveVoteRepository.findAllByChatRoom_Id(roomId);
        List<User> userList = new ArrayList<>();
        for (SaveVote saveVote : saveVotesList) {
            userList.add(saveVote.getUser());
        }
//        List<User> userList = vote.getUserList();

        for (User user : userList) {
            VoteUserListResponseDto voteUserListResponseDto = new VoteUserListResponseDto(user);
            voteUserListResponseDtoList.add(voteUserListResponseDto);
        }
        return voteUserListResponseDtoList;
    }

}