package com.flab.rallymate.applicant.service;

import java.time.LocalDateTime;

import com.flab.rallymate.applicant.domain.ApplicantRequestDTO;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.ErrorCode;
import com.flab.rallymate.rallyschedule.RallyScheduleService;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;

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

		if(isAlreadyStarted(rallySchedule)) {
			throw new BaseException(ErrorCode.ALREADY_STARTED);
		}

		if(isAlreadyApplied(rallySchedule)) {
			throw new BaseException(ErrorCode.ALREADY_APPLIED);
		}

		if(isExceedMaxApplicant(rallySchedule)) {
			throw new BaseException(ErrorCode.EXCEED_MAX_APPLICANT);
		}
        applicantService.addApplicant(rallySchedule);
    }

	private boolean isAlreadyStarted(RallyScheduleEntity rallySchedule) {
		return LocalDateTime.now().isAfter(rallySchedule.getStartTime());
	}

	private boolean isAlreadyApplied(RallyScheduleEntity rallySchedule) {
		return applicantService.isAlreadyApplied(rallySchedule);
	}

	private boolean isExceedMaxApplicant(RallyScheduleEntity rallySchedule) {
		return rallySchedule.getMaxApplicant() <= applicantService.getApplicantCount(rallySchedule);
	}
}
