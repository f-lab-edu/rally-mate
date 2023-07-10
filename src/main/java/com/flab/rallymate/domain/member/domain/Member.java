package com.flab.rallymate.domain.member.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.flab.rallymate.domain.member.constant.Status;
import com.flab.rallymate.domain.member.constant.UserRole;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

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
	@Column(nullable = false)
	private Status status;

	@Column(name = "created_time", nullable = false)
	private LocalDateTime createdTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private UserRole userRole;

	@Builder
	public Member(Long id, String name, String email, String password, Status status, UserRole userRole, LocalDateTime createdTime) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.status = status;
		this.userRole = Objects.isNull(userRole) ? UserRole.ROLE_USER : userRole;
		this.createdTime = createdTime;
	}

	public static Member createMember(String name, String email, String password, UserRole userRole) {
		return Member.builder()
			.name(name)
			.email(email)
			.password(password)
			.status(Status.USED)
			.createdTime(LocalDateTime.now())
			.userRole(userRole)
			.build();
	}
}
