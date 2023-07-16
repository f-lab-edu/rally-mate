package com.flab.rallymate.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

public record KakaoUserResponseDTO(
	String id,
	Properties properties,

	@JsonProperty("kakao_account")
	KakaoAccount kakaoAccount
) {

	@Builder
	public KakaoUserResponseDTO {
	}
}



