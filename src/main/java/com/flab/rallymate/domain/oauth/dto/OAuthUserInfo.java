package com.flab.rallymate.domain.oauth.dto;

import lombok.Builder;

public record OAuthUserInfo(
	String id,
	String email,
	String nickname
) {
	@Builder
	public OAuthUserInfo {
	}

	public static OAuthUserInfo of(String id, String email, String nickname) {
		return OAuthUserInfo.builder()
			.id(id)
			.email(email)
			.nickname(nickname)
			.build();
	}

}
