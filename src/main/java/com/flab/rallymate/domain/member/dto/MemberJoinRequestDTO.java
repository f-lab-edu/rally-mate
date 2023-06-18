package com.flab.rallymate.domain.member.dto;

import lombok.Builder;

public record MemberJoinRequestDTO(
		String name,
		String email,
		String password
) {
	@Builder
	public MemberJoinRequestDTO {}

	public static MemberJoinRequestDTO of(String name, String email, String password) {
		return MemberJoinRequestDTO.builder()
			.name(name)
			.email(email)
			.password(password)
			.build();
	}
}
