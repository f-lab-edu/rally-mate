package com.flab.rallymate.applicant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flab.rallymate.applicant.domain.ApplicantEntity;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;

public interface ApplicantRepository extends JpaRepository<ApplicantEntity, Long> {

	boolean existsByMemberAndRallySchedule(MemberEntity member, RallyScheduleEntity rallySchedule);

	int countByRallySchedule(RallyScheduleEntity rallySchedule);
}
