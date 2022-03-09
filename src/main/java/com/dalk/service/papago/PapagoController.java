package com.dalk.service.papago;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class PapagoController {

    @PostMapping("/papago")
    public String papago(@RequestBody PapagoRequestDto requestDto) throws IOException {
        String string = requestDto.getText();
        return PapagoService.papago(string);
    }
}
