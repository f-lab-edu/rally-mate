package com.flab.rallymate.domain.member.constant;

import lombok.Getter;

@Getter
public enum MemberStatus {

	ACTIVATE("활성화"),
	BLOCKED("차단"),
	DORMANT("휴면");

	private final String status;

	MemberStatus(String status) {
		this.status = status;
	}
}
