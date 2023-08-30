package com.flab.rallymate.applicant.service;

import com.flab.rallymate.applicant.domain.ApplicantRequestDTO;
import com.flab.rallymate.rallyschedule.RallyScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RallyApplicationService {

    private final RallyScheduleService rallyScheduleService;
    private final ApplicantService applicantService;

    public void applyRallySchedule(ApplicantRequestDTO applicantRequestDTO) {
        var rallySchedule = rallyScheduleService.findRallyScheduleBy(applicantRequestDTO.scheduleId());
        applicantService.addApplicant(rallySchedule);
    }
}
