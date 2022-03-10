package com.dalk.service.papago;

import com.dalk.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@AllArgsConstructor
@RestController
public class PapagoController {

    @PostMapping("/papago")
    public String papago(@RequestBody PapagoRequestDto requestDto) throws IOException, NoSuchAlgorithmException {
        String string = requestDto.getText();
        return ItemService.papago(string);
    }

    @PostMapping("/reverse")
    public String reverseItem(@RequestBody PapagoRequestDto requestDto){
        String string = requestDto.getText();
        return ItemService.reverseWord(string);
    }
}
