package com.flab.rallymate.applicant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flab.rallymate.applicant.domain.ApplicantRequestDTO;
import com.flab.rallymate.applicant.service.ApplicantService;
import com.flab.rallymate.applicant.service.RallyApplicationService;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.ErrorCode;
import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.rallyschedule.RallyScheduleService;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;

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

		var applicantRequestDTO = ApplicantRequestDTO.builder().scheduleId(1L).build();
		var rallySchedule = RallyScheduleEntity.builder().id(1L)
			.maxApplicant(2)
			.startTime(LocalDateTime.now().plusHours(1L))
			.build();
		when(rallyScheduleService.findRallyScheduleBy(applicantRequestDTO.scheduleId())).thenReturn(rallySchedule);

		sut.applyRallySchedule(applicantRequestDTO);

		verify(rallyScheduleService, times(1)).findRallyScheduleBy(applicantRequestDTO.scheduleId());
		verify(applicantService, times(1)).addApplicant(rallySchedule);
	}

	@Test
	void applyRallySchedule_이미_요청한_참여자가_참여_재요청시에_실패한다() {
		var applicantRequestDTO = ApplicantRequestDTO.builder().scheduleId(1L).build();
		var rallySchedule = RallyScheduleEntity.builder().id(1L)
			.maxApplicant(2)
			.startTime(LocalDateTime.now().plusHours(1L))
			.build();
		when(rallyScheduleService.findRallyScheduleBy(applicantRequestDTO.scheduleId())).thenReturn(rallySchedule);
		when(applicantService.isAlreadyApplied(rallySchedule)).thenReturn(true);


		var baseException = assertThrows(BaseException.class, () -> sut.applyRallySchedule(applicantRequestDTO));


		assertEquals(ErrorCode.ALREADY_APPLIED.getMessage(), baseException.getMessage());
		verify(rallyScheduleService, times(1)).findRallyScheduleBy(applicantRequestDTO.scheduleId());
		verify(applicantService, times(0)).addApplicant(rallySchedule);
		verify(applicantService, times(1)).isAlreadyApplied(rallySchedule);
	}

	@Test
	void applyRallySchedule_시작일이_이미_지난_랠리스케쥴에_참여요청시_실패한다() {
		var applicantRequestDTO = ApplicantRequestDTO.builder().scheduleId(1L).build();
		var rallySchedule = RallyScheduleEntity.builder().id(1L)
			.startTime(LocalDateTime.now().minusHours(1L)).build();
		when(rallyScheduleService.findRallyScheduleBy(applicantRequestDTO.scheduleId())).thenReturn(rallySchedule);


		var baseException = assertThrows(BaseException.class, () -> sut.applyRallySchedule(applicantRequestDTO));


		assertEquals(ErrorCode.ALREADY_STARTED.getMessage(), baseException.getMessage());
	}

	@Test
	void applyRallySchedule_정원초과된_랠리스케쥴에_참여요청시_실패한다() {
		var applicantRequestDTO = ApplicantRequestDTO.builder().scheduleId(1L).build();
		var rallySchedule = RallyScheduleEntity.builder().id(1L)
			.maxApplicant(2)
			.startTime(LocalDateTime.now().plusHours(1L))
			.build();
		when(rallyScheduleService.findRallyScheduleBy(applicantRequestDTO.scheduleId())).thenReturn(rallySchedule);
		when(applicantService.getApplicantCount(rallySchedule)).thenReturn(3);


		var baseException = assertThrows(BaseException.class, () -> sut.applyRallySchedule(applicantRequestDTO));


		assertEquals(ErrorCode.EXCEED_MAX_APPLICANT.getMessage(), baseException.getMessage());
	}

}
