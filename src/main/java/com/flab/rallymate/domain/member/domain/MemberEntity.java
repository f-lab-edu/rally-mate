package com.flab.rallymate.domain.member.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.flab.rallymate.domain.member.constant.MemberStatus;
import com.flab.rallymate.domain.member.constant.UserRole;

import com.flab.rallymate.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private MemberStatus memberStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private UserRole userRole;

	@Builder
	public MemberEntity(Long id, String name, String email, String password, MemberStatus memberStatus, UserRole userRole) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.memberStatus = memberStatus;
		this.userRole = Objects.isNull(userRole) ? UserRole.ROLE_USER : userRole;
		setCreatedTime(LocalDateTime.now());
	}

	public static MemberEntity createMember(String name, String email, String password, UserRole userRole) {
		return MemberEntity.builder()
			.name(name)
			.email(email)
			.password(password)
			.memberStatus(MemberStatus.ACTIVATE)
			.userRole(userRole)
			.build();
	}


}
