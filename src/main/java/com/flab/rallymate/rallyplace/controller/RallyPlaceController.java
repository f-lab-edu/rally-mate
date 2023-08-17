package com.flab.rallymate.rallyplace.controller;

import com.flab.rallymate.rallyplace.RallyPlaceService;
import com.flab.rallymate.rallyplace.domain.RallyPlaceRequestDTO;
import com.flab.rallymate.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class RallyPlaceController {

    private final RallyPlaceService rallyPlaceService;

    @PostMapping
    @Operation(summary = "랠리 장소 등록 API")
    public BaseHttpResponse<String> addRallyPlace(@RequestBody RallyPlaceRequestDTO rallyPlaceRequestDTO) {
        rallyPlaceService.addRallyPlace(rallyPlaceRequestDTO);
        return BaseHttpResponse.successWithNoContent("장소를 등록했어요!");
    }
}