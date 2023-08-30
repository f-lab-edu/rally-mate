package com.flab.rallymate.applicant.controller;

import com.flab.rallymate.applicant.domain.ApplicantRequestDTO;
import com.flab.rallymate.applicant.service.RallyApplicationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.rallymate.global.BaseHttpResponse;
import com.flab.rallymate.rallyplace.RallyPlaceService;
import com.flab.rallymate.rallyplace.domain.RallyPlaceRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/applicant")
@RequiredArgsConstructor
public class ApplicantController {

    private final RallyApplicationService rallyApplicationService;

    @PostMapping
    @Operation(summary = "랠리 스케쥴 참여 요청 API")
    public BaseHttpResponse<String> applyRallySchedule(@RequestBody ApplicantRequestDTO applicantRequestDTO) {
        rallyApplicationService.applyRallySchedule(applicantRequestDTO);
        return BaseHttpResponse.successWithNoContent("참여 요청에 성공 했습니다.");
    }
}
