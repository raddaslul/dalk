package com.dalk.service;

import com.dalk.config.auth.UserDetailsImpl;
import com.dalk.domain.Board;
import com.dalk.dto.responseDto.MainPageResponse.MainPageAllResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageBoardResponseDto;
import com.dalk.dto.responseDto.MainPageResponse.MainPageTop6ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class MainPageService {



    //채팅방 생성
    public void createChatRoom(UserDetailsImpl userDetails) {
    }
    //토론방리스트 탑6 조회
    public MainPageTop6ResponseDto getMainPageTop6() {
        return new MainPageTop6ResponseDto();
    }
    //토론방리스트 전체조회
    public MainPageAllResponseDto getMainPageAll() {
        return new MainPageAllResponseDto();
    }
    //게시글 전체 조회
    public MainPageBoardResponseDto getMainPageBoard() {
        return new MainPageBoardResponseDto();
    }
    //게시글 상세 조회
    public MainPageBoardResponseDto getMainPageBoardDetail(Long boardId) {
        return new MainPageBoardResponseDto();
    }

}
