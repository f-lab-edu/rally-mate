package com.flab.rallymate.rallyschedule.controller;

import com.flab.rallymate.rallyschedule.RallyScheduleService;
import com.flab.rallymate.rallyschedule.domain.RallyScheduleRequestDTO;
import com.flab.rallymate.global.BaseHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class RallyScheduleController {

    private final RallyScheduleService rallyScheduleService;

    @PostMapping
    @Operation(summary = "랠리 일정 등록 API")
    public BaseHttpResponse<String> addRallySchedule(@Valid @RequestBody RallyScheduleRequestDTO rallyScheduleRequestDTO) {
        rallyScheduleService.addRallySchedule(rallyScheduleRequestDTO);
        return BaseHttpResponse.successWithNoContent("구인글을 등록했어요.");
    }
}