package com.flab.rallymate.domain.member.constant;

import lombok.Getter;

@Getter
public enum Status {

	USED("사용중"),
	SUSPEND("사용정지"),
	DELETED("삭제됨");

	private final String status;

	Status(String status) {
		this.status = status;
	}
}
