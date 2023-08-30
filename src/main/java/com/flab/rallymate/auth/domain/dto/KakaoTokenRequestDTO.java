package com.flab.rallymate.auth.domain.dto;

import com.flab.rallymate.config.KakaoOAuthProperties;

public record KakaoTokenRequestDTO(
	String grantType,
	String clientId,
	String redirectURI,
	String code
) {

	public static KakaoTokenRequestDTO createKakaoTokenRequestDTO(KakaoOAuthProperties kakaoOAuthProperties,
		String authCode) {
		return new KakaoTokenRequestDTO("authorization_code", kakaoOAuthProperties.getClientId(),
			kakaoOAuthProperties.getCallbackUrl(), authCode);
	}

	@Override
	public String toString() {
		return "code=" + code + '&' +
			"client_id=" + clientId + '&' +
			"redirect_uri=" + redirectURI + '&' +
			"grant_type=" + grantType;
	}
}