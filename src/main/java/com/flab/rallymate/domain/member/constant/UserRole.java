package com.flab.rallymate.domain.member.constant;

import lombok.Getter;

@Getter
public enum UserRole {

	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_USER("ROLE_USER");

	private final String value;

	UserRole(String value) {
		this.value = value;
	}
}
