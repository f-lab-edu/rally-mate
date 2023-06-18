package com.flab.rallymate.auth.dto;

import lombok.Builder;

public record LoginResponseDTO(
	Long id,
	String accessToken
) {
	@Builder
	public LoginResponseDTO {
	}

	public static LoginResponseDTO of(Long id, String accessToken) {
		return LoginResponseDTO.builder()
			.id(id)
			.accessToken(accessToken)
			.build();
	}
}
