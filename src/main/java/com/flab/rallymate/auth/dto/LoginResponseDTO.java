package com.flab.rallymate.auth.dto;

import lombok.Builder;

public record LoginResponseDTO(
	Long id,
	String accessToken,
	String refreshToken
) {
	@Builder
	public LoginResponseDTO {
	}

	public static LoginResponseDTO of(Long id, String accessToken, String refreshToken) {
		return LoginResponseDTO.builder()
			.id(id)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
