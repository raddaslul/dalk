package com.dalk.controller;

import com.dalk.domain.User;
import com.dalk.dto.responseDto.LottoResponseDto;
import com.dalk.security.UserDetailsImpl;
import com.dalk.service.LottoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class LottoController {
    private final LottoService lottoService;

    @GetMapping("/lotto")
    @ApiOperation(value = "로또뽑기")
    public LottoResponseDto getLotto(@AuthenticationPrincipal UserDetailsImpl userDetails) throws NoSuchAlgorithmException {
        User user = userDetails.getUser();
        return lottoService.getLotto(user);
    }
}
