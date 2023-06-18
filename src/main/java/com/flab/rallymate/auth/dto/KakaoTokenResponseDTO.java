package com.flab.rallymate.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResponseDTO(
	@JsonProperty("access_token")
	String accessToken,

	@JsonProperty("refresh_token")
	String refreshToken
) {
}
