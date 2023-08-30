package com.flab.rallymate.applicant.domain;

import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;

import lombok.Builder;

public record ApplicantRequestDTO(
	Long scheduleId
) {
	@Builder
	public ApplicantRequestDTO {
	}

	public ApplicantEntity toApplicantEntity(MemberEntity member, RallyScheduleEntity rallySchedule) {
		return ApplicantEntity.builder()
			.id(scheduleId())
			.member(member)
			.rallySchedule(rallySchedule)
			.build();
	}
}
