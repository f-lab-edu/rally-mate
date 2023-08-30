package com.flab.rallymate.applicant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.rallymate.applicant.domain.ApplicantRequestDTO;
import com.flab.rallymate.applicant.repository.ApplicantRepository;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.ErrorCode;
import com.flab.rallymate.member.MemberService;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicantService {

	private final ApplicantRepository applicantRepository;
	private final MemberService memberService;

	public void addApplicant(ApplicantRequestDTO applicantRequestDTO, RallyScheduleEntity rallySchedule) {
		String email = memberService.getCurrentMemberEmail();
		MemberEntity member = memberService.findMemberBy(email)
			.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MEMBER));
		applicantRepository.save(applicantRequestDTO.toApplicantEntity(member, rallySchedule));
	}
}
