package com.flab.rallymate.auth.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

public record KakaoUserResponseDTO(
	String id,

	@JsonProperty("properties")
	KakaoUserPropertiesDTO kakaoUserPropertiesDTO,

	@JsonProperty("kakao_account")
    KakaoAccountDTO kakaoAccountDTO
) {

	@Builder
	public KakaoUserResponseDTO {
	}
}



