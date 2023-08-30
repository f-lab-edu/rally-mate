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
	void applyRallySchedule_랠리스케쥴_신청요청에_성공한다() {

		var applicantRequestDTO = ApplicantRequestDTO.builder()
			.scheduleId(1L)
			.build();
		var rallySchedule = RallyScheduleEntity.builder().build();
		String email = "test@test.com";
		MemberEntity member = MemberEntity.builder().build();


		// 1. 회원 이메일을 가져온다.
		when(memberService.getCurrentMemberEmail()).thenReturn(email);
		// 2. 회원 이메일을 통해 Entity를 조회한다.
		when(memberService.findMemberBy(email)).thenReturn(Optional.of(member));
		// 3. 신청할 랠리스케쥴을 조회한다. (byid)
		when(rallyScheduleService.findRallyScheduleBy(applicantRequestDTO.scheduleId())).thenReturn(rallySchedule);
		// 4. Applicant Entity를 생성한다.
		// 5. Applicant Entity를 저장한다.

		// when(applicantService.addApplicant(applicantRequestDTO, rallySchedule));


		sut.applyRallySchedule(applicantRequestDTO);


		// verify(ra, times(1)).apply(applicantRequestDTO);
	}
}
