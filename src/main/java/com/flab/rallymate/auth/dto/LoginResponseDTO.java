package com.flab.rallymate.auth.dto;

import lombok.Builder;

public record LoginResponseDTO(
	Long memberId,
	String accessToken,
	String refreshToken
) {
	@Builder
	public LoginResponseDTO {
	}

	public static LoginResponseDTO of(Long memberId, String accessToken, String refreshToken) {
		return LoginResponseDTO.builder()
			.memberId(memberId)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
