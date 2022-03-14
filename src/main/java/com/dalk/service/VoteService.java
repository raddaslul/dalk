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
    @Transactional
    public void saveVote(Long roomId, User user, VoteRequestDto requestDto) {
        Vote vote = voteRepository.findByChatRoom_Id(roomId); //투표 찾아서
        SaveVote savevote = saveVoteRepository.findByUser_IdAndChatRoom_IdAndVote_Id(user.getId(), roomId, vote.getId());//같은 유저가 투표한적이 있으면 null이 아님
        if (savevote != null) {
            throw new DuplicateVoteException("이미 투표에 참여하셨습니다");
        }
        if (user.getTotalPoint() < requestDto.getPoint()) //내가 보유한 포인트 보다 많을경우
        {
            throw new LackPointException("보유한 포인트가 부족합니다");
        }

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new ChatRoomNotFoundException("해당 채팅방이 없습니다")
        );

        user.setTotalPoint(user.getTotalPoint() - requestDto.getPoint()); //유저의 포인트 변화
        userRepository.save(user); //저장
        Point point = new Point("투표 참여", -requestDto.getPoint(), user.getTotalPoint(), user); //포인트 내역 생성
        pointRepository.save(point); //포인트 내역 저장

        if (requestDto.getTopic()) { // topicA를 골랐을 때
            SaveVote saveVote = new SaveVote(true, chatRoom, requestDto.getPoint(), vote, user); // savevote 추가
            saveVoteRepository.save(saveVote); //저장
            vote.setTotalPointA(vote.getTotalPointA() + requestDto.getPoint()); //totalpoint 추가
            vote.setTopicACnt(vote.getTopicACnt() + 1);
            if (vote.getTopPointA() < requestDto.getPoint()) { //toppoint찾기
                vote.setTopPointA(requestDto.getPoint());
            }
            voteRepository.save(vote);
        } else {//topicB를 골랐을 때
            SaveVote saveVote = new SaveVote(false, chatRoom, requestDto.getPoint(), vote, user);
            saveVoteRepository.save(saveVote); //저장
            vote.setTotalPointB(vote.getTotalPointB() + requestDto.getPoint()); //totalpoint 추가
            vote.setTopicBCnt(vote.getTopicBCnt() + 1);
            if (vote.getTopPointB() < requestDto.getPoint()) { //toppoint찾기
                vote.setTopPointB(requestDto.getPoint());
            }
            voteRepository.save(vote);
        }


    }

    //토론방 유저 리스트
    @Transactional
    public List<VoteUserListResponseDto> voteUserList(Long roomId) {
        List<VoteUserListResponseDto> voteUserListResponseDtoList = new ArrayList<>();
        List<SaveVote> saveVotesList = saveVoteRepository.findAllByChatRoom_Id(roomId);
        List<User> userList = new ArrayList<>();
        for (SaveVote saveVote : saveVotesList) {
            userList.add(saveVote.getUser());
        }

        for (User user : userList) {
            VoteUserListResponseDto voteUserListResponseDto = new VoteUserListResponseDto(user);
            voteUserListResponseDtoList.add(voteUserListResponseDto);
        }
        return voteUserListResponseDtoList;
    }

    //투표 누가 이겼는지 확인
    @Transactional
    public void winVote(Long roomId) {
        Float winRate;
        Vote vote = voteRepository.findByChatRoom_Id(roomId); //투표 찾아서
        List<SaveVote> saveVotesTrueList = saveVoteRepository.findAllByChatRoom_IdAndPickTrue(roomId); // topicA픽한사람들 모임
        List<SaveVote> saveVotesFalseList = saveVoteRepository.findAllByChatRoom_IdAndPickFalse(roomId); // topicB픽한사람들 모임
        List<SaveVote> saveVotesTieList = saveVoteRepository.findAllByChatRoom_Id(roomId);
        List<User> userWinnerList = new ArrayList<>(); //승리한 유저들
        Float totalPoint = vote.getTotalPointA() + vote.getTotalPointB();

        if (vote.getTopicACnt() > vote.getTopicBCnt()) { // topicA가 이겻을 경우
            winRate = (totalPoint / vote.getTotalPointA()); //배당률 계산
            for (SaveVote saveVote : saveVotesTrueList) { //승리한 유저 리스트에 해당하는 픽을 한 유저들을 집어넣음
                userWinnerList.add(saveVote.getUser());
            }
            for (User user : userWinnerList) {
                SaveVote saveVote = saveVoteRepository.findByUser_IdAndChatRoom_Id(user.getId(), roomId); //유저와 채팅방 id로 savevote를 뽑아옴 (유저는 한개씩 가짐)
                user.setTotalPoint((long) (user.getTotalPoint() + (saveVote.getPoint() * winRate))); //savevote에서 내가 얼마를 걸었는지가 있는데 거기서 뽑아와서 winRate계산을 함
                userRepository.save(user);
                Point point = new Point("투표 승리", (long) (saveVote.getPoint() * winRate), user.getTotalPoint(), user); //포인트 내역 생성
                pointRepository.save(point);
            }
        } else if (vote.getTopicACnt() < vote.getTopicBCnt()) { //topicB가 이겼을 경우
            winRate = (totalPoint / vote.getTotalPointB()); // 배당률 계산
            for (SaveVote saveVote : saveVotesFalseList) { //승리한 유저 리스트에 해당하는 픽을 한 유저들을 집어넣음
                userWinnerList.add(saveVote.getUser());
            }
            for (User user : userWinnerList) {
                SaveVote saveVote = saveVoteRepository.findByUser_IdAndChatRoom_Id(user.getId(), roomId); //유저와 채팅방 id로 savevote를 뽑아옴 (유저는 한개씩 가짐)
                user.setTotalPoint((long) (user.getTotalPoint() + (saveVote.getPoint() * winRate))); //savevote에서 내가 얼마를 걸었는지가 있는데 거기서 뽑아와서 winRate계산을 함
                userRepository.save(user);
                Point point = new Point("투표 승리", (long) (saveVote.getPoint() * winRate), user.getTotalPoint(), user); //포인트 내역 생성
                pointRepository.save(point);
            }
        } else {//투표가 동률일경우
            for (SaveVote saveVote : saveVotesTieList) {
                userWinnerList.add(saveVote.getUser());
            }
            for (User user : userWinnerList) {
                SaveVote saveVote = saveVoteRepository.findByUser_IdAndChatRoom_Id(user.getId(), roomId); //유저와 채팅방 id로 savevote를 뽑아옴 (유저는 한개씩 가짐)
                user.setTotalPoint((user.getTotalPoint() + (saveVote.getPoint()))); //savevote에서 내가 얼마를 걸었는지가 있는데 거기서 뽑아와서 winRate계산을 함
                userRepository.save(user);
                Point point = new Point("투표 무승부", (saveVote.getPoint()), user.getTotalPoint(), user); //포인트 내역 생성
                pointRepository.save(point);
            }
        }
    }
}