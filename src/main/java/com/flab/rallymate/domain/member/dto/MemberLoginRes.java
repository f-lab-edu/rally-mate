package com.flab.rallymate.domain.member.dto;

import lombok.Builder;

public record MemberLoginRes(
	Long id,
	String jwt
) {
	@Builder
	public MemberLoginRes {
	}

	public static MemberLoginRes of(Long id, String jwt) {
		return MemberLoginRes.builder()
			.id(id)
			.jwt(jwt)
			.build();
	}
}
