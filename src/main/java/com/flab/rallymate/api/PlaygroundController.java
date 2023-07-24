package com.flab.rallymate.api;

import com.flab.rallymate.domain.playground.PlaygroundService;
import com.flab.rallymate.domain.playground.dto.PlaygroundRequestDTO;
import com.flab.rallymate.error.BaseHttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @PostMapping
    public BaseHttpResponse<String> addPlace(@RequestBody PlaygroundRequestDTO playgroundRequestDTO) {
        playgroundService.addPlace(playgroundRequestDTO);
        return BaseHttpResponse.successWithNoContent("장소를 등록했어요!");
    }
}