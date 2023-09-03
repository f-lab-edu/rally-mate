package com.flab.rallymate.applicant;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flab.rallymate.applicant.domain.ApplicantEntity;
import com.flab.rallymate.applicant.repository.ApplicantRepository;
import com.flab.rallymate.applicant.service.ApplicantService;
import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;

public class ApplicantServiceTest {
	private ApplicantService sut;
	private ApplicantRepository applicantRepository;
	private MemberService memberService;

	@BeforeEach
	void setUp() {
		applicantRepository = mock(ApplicantRepository.class);
		memberService = mock(MemberService.class);
		sut = new ApplicantService(applicantRepository, memberService);
	}

	@Test
	void addApplicant_랠리스케쥴_참여요청에_성공한다() {
		String email = "test@test.com";
		var member = MemberEntity.builder().build();
		var rallySchedule = RallyScheduleEntity.builder().build();
		ApplicantEntity applicantEntity = ApplicantEntity.builder()
			.member(member)
			.rallySchedule(rallySchedule)
			.build();

		when(memberService.getCurrentMemberEmail()).thenReturn(email);
		when(memberService.findMemberBy(email)).thenReturn(Optional.of(member));


		sut.addApplicant(rallySchedule);


		verify(applicantRepository, times(1)).save(applicantEntity);
	}


}
