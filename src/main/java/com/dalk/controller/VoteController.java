package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.requestDto.VoteRequestDto;
import com.dalk.dto.responseDto.VoteUserListResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.VoteService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping("/vote/{roomId}")
    @ApiOperation(value = "투표")
    public void vote(@PathVariable Long roomId,
                     @AuthenticationPrincipal UserDetailsImpl userDetails,
                     @RequestBody VoteRequestDto requestDto) {
        User user = userDetails.getUser();
        voteService.saveVote(roomId, user, requestDto);
    }

    @GetMapping("/vote/{roomId}")
    @ApiOperation(value = "투표자 명단")
    public List<VoteUserListResponseDto> voteUserList(@PathVariable Long roomId) {
        return voteService.voteUserList(roomId);
    }

    @GetMapping("/winvote/{roomId}")
    @ApiOperation(value = "투표 종료")
    public HashMap<Object, String> winVote(@PathVariable Long roomId) {
        voteService.winVote(roomId);
        HashMap<Object, String> result = new HashMap<>();
        result.put("roomId", String.valueOf(roomId));
        return result;
    }
}
