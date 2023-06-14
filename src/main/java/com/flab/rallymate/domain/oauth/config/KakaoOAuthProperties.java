package com.flab.rallymate.domain.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Validated
@ConfigurationProperties(prefix = "oauth2.client.kakao")
public class KakaoOAuthProperties {
	
	@NotBlank
	private final String clientId;

	@NotBlank
	private final String clientSecret;

	@NotBlank
	private final String callbackUrl;

	@ConstructorBinding
	public KakaoOAuthProperties(String clientId, String clientSecret, String callbackUrl) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.callbackUrl = callbackUrl;
	}
}
