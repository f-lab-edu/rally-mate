package com.flab.rallymate.applicant.domain;

import java.util.Objects;

import com.flab.rallymate.global.BaseEntity;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.member.enums.MemberStatus;
import com.flab.rallymate.member.enums.UserRole;
import com.flab.rallymate.rallyschedule.domain.entity.RallyScheduleEntity;
import com.flab.rallymate.applicant.enums.ApplicantStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "applicant")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class ApplicantEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "status", nullable = false)
	private ApplicantStatus requestStatus;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberEntity member;

	@JoinColumn(name = "rally_schedule_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private RallyScheduleEntity rallySchedule;

	@PrePersist
	public void prePersist() {
		this.requestStatus = Objects.isNull(this.requestStatus) ? ApplicantStatus.REQUESTED : this.requestStatus;
	}

	private ApplicantEntity(Long id, ApplicantStatus requestStatus, MemberEntity member,
		RallyScheduleEntity rallySchedule) {
		this.id = id;
		this.requestStatus = requestStatus;
		this.member = member;
		this.rallySchedule = rallySchedule;
	}
}
