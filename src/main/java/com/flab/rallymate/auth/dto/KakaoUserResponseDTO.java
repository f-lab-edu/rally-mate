package com.flab.rallymate.auth.dto;

import lombok.Builder;

public record KakaoUserResponseDTO(
	Long id,
	String nickname,
	String email
) {
	@Builder
	public KakaoUserResponseDTO{}

	public static KakaoUserResponseDTO of(Long id, String nickname, String email) {
		return KakaoUserResponseDTO.builder()
			.id(id)
			.nickname(nickname)
			.email(email)
			.build();
	}


}
