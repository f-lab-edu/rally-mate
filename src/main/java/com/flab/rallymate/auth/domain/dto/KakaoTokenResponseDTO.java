package com.flab.rallymate.auth.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

public record KakaoTokenResponseDTO(
	@JsonProperty("access_token")
	String accessToken,

	@JsonProperty("refresh_token")
	String refreshToken
) {
	@Builder
	public KakaoTokenResponseDTO {
	}
}
