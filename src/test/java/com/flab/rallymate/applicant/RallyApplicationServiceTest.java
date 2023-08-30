package com.flab.rallymate.applicant;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flab.rallymate.applicant.domain.ApplicantRequestDTO;
import com.flab.rallymate.applicant.repository.ApplicantRepository;
import com.flab.rallymate.applicant.service.ApplicantService;
import com.flab.rallymate.applicant.service.RallyApplicationService;
import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyschedule.RallyScheduleService;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;
import com.flab.rallymate.rallyschedule.repository.RallyScheduleRepository;

class RallyApplicationServiceTest {

	private RallyApplicationService sut;
	private RallyScheduleService rallyScheduleService;
	private ApplicantService applicantService;
	private MemberService memberService;

	@BeforeEach
	void setUp() {
		rallyScheduleService = mock(RallyScheduleService.class);
		applicantService = mock(ApplicantService.class);
		sut = new RallyApplicationService(rallyScheduleService, applicantService);
	}

	@Test
	void applyRallySchedule_랠리스케쥴_참여요청에_성공한다() {

		String email = "test@test.com";
		var member = MemberEntity.builder().build();
		var applicantRequestDTO = ApplicantRequestDTO.builder().scheduleId(1L).build();
		var rallySchedule = RallyScheduleEntity.builder().build();
		when(rallyScheduleService.findRallyScheduleBy(applicantRequestDTO.scheduleId())).thenReturn(rallySchedule);


		sut.applyRallySchedule(applicantRequestDTO);


		 verify(rallyScheduleService, times(1)).findRallyScheduleBy(applicantRequestDTO.scheduleId());
		 verify(applicantService, times(1)).addApplicant(rallySchedule);
	}
}
