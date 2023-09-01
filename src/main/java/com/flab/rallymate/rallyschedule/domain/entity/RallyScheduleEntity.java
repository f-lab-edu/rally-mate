package com.flab.rallymate.rallyschedule.domain.entity;

import java.time.LocalDateTime;

import com.flab.rallymate.global.BaseEntity;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.rallyplace.domain.RallyPlaceEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rally_schedule")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class RallyScheduleEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "play_time", nullable = false)
	private int playTime;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name="max_applicant")
	private int maxApplicant;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberEntity member;

	@JoinColumn(name = "rally_place_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private RallyPlaceEntity rallyPlace;

	private RallyScheduleEntity(Long id, int playTime, LocalDateTime startTime, int maxApplicant, MemberEntity member,
		RallyPlaceEntity rallyPlace) {
		this.id = id;
		this.playTime = playTime;
		this.startTime = startTime;
		this.maxApplicant = maxApplicant;
		this.member = member;
		this.rallyPlace = rallyPlace;
	}
}
