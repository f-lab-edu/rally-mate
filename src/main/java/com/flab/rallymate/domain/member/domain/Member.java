package com.flab.rallymate.domain.member.domain;

import java.time.LocalDateTime;

import com.flab.rallymate.domain.member.constant.Status;

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

	@Builder
	private Member(Long id, String name, String email, String password, Status status, LocalDateTime createdTime) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.status = status;
		this.createdTime = createdTime;
	}

	public static Member createMember(String name, String email, String password) {
		System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
		return Member.builder()
			.name(name)
			.email(email)
			.password(password)
			.status(Status.USED)
			.createdTime(LocalDateTime.now())
			.build();
	}
}
