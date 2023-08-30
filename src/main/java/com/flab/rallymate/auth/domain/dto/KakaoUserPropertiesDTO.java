package com.flab.rallymate.auth.domain.dto;

import lombok.Builder;

public record KakaoUserPropertiesDTO(
	String nickname
) {
	@Builder
	public KakaoUserPropertiesDTO {}
}
