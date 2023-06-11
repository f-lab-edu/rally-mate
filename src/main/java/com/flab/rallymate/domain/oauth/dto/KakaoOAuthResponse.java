package com.flab.rallymate.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KakaoOAuthResponse {

	private String accessToken;
	private String expiresIn;
	private String tokenType;
	private String scope;
	private String idToken;
	private String refreshToken;

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}

