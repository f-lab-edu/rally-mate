package com.flab.rallymate.member.domain;

import com.flab.rallymate.global.BaseEntity;
import com.flab.rallymate.member.enums.MemberStatus;
import com.flab.rallymate.member.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Builder
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

	@Column(nullable = false)
	private Integer career;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private MemberStatus memberStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private UserRole userRole;

	@PrePersist
	public void prePersist() {
		this.memberStatus = Objects.isNull(this.memberStatus) ? MemberStatus.ACTIVATE : this.memberStatus;
		this.userRole = Objects.isNull(this.userRole) ? UserRole.ROLE_USER : this.userRole;
	}

	private MemberEntity(Long id, String name, String email, String password, Integer career, MemberStatus memberStatus, UserRole userRole) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.career = career;
		this.memberStatus = memberStatus;
		this.userRole = userRole;
	}
}