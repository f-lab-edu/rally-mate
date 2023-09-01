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

		var applicantRequestDTO = ApplicantRequestDTO.builder().scheduleId(1L).build();
		var rallySchedule = RallyScheduleEntity.builder().build();
		when(rallyScheduleService.findRallyScheduleBy(applicantRequestDTO.scheduleId())).thenReturn(rallySchedule);


		sut.applyRallySchedule(applicantRequestDTO);


		 verify(rallyScheduleService, times(1)).findRallyScheduleBy(applicantRequestDTO.scheduleId());
		 verify(applicantService, times(1)).addApplicant(rallySchedule);
	}

	@Test
	void applyRallySchedule_이미_요청한_참여자가_참여_재요청시에_실패한다() {
		// (1) 무엇을 테스트할 것인가 : 랠리 스케쥴 참여 요청
	  // (2) 어떤 상황에서 ? : 특정 스케쥴에 참여 요청을 보낸 사용자가 재 참여 요청을 보내는 상황
	  // (3) 기대하는 결과는? : "이미 참여 요청하였습니다" 메시지, 참여 요청 실패

	}

	@Test
	void applyRallySchedule_존재하지_않는_랠리스케쥴에_참여요청시_실패한다() {

	}

	@Test
	void applyRallySchedule_시작일이_이미_지난_랠리스케쥴에_참여요청시_실패한다() {

	}

	@Test
	void applyRallySchedule_이미_종료된_랠리스케쥴에_참여요청시_실패한다() {

	}

	@Test
	void applyRallySchedule_정원초과된_랠리스케쥴에_참여요청시_실패한다() {

	}

}
