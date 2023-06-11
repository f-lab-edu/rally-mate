package com.flab.rallymate.domain.member.dto;

import lombok.Builder;

public record MemberJoinReq(
		String name,
		String email,
		String password
) {
	@Builder
	public MemberJoinReq {}

	public static MemberJoinReq of(String name, String email, String password) {
		return MemberJoinReq.builder()
			.name(name)
			.email(email)
			.password(password)
			.build();
	}
}
