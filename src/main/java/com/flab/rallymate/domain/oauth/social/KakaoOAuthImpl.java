package com.flab.rallymate.domain.oauth.social;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.flab.rallymate.domain.oauth.config.OAuthProperties;
import com.flab.rallymate.domain.oauth.dto.OAuthUserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KakaoOAuthImpl implements SocialOAuth {

	private final OAuthProperties oAuthProperties;

	public KakaoOAuthImpl(OAuthProperties oAuthProperties) {
		this.oAuthProperties = oAuthProperties;
	}

	@Override
	public String getOAuthRedirectUrl() {
		StringBuffer url = new StringBuffer();
		url.append("https://kauth.kakao.com/oauth/authorize?");
		url.append("client_id=").append(oAuthProperties.getKakao().getClientId());
		url.append("&redirect_uri=").append(oAuthProperties.getKakao().getCallbackUrl());
		url.append("&response_type=code");

		return url.toString();
	}

	@Override
	public OAuthUserInfo getUserInfo(String authorizedCode) throws Exception {
		String accessToken = requestAccessToken(authorizedCode);
		OAuthUserInfo userInfo = getUserInfoByToken(accessToken);

		return userInfo;
	}

	@Override
	public String getInitPassword() {
		return oAuthProperties.getKakao().getClientSecret();
	}

	// ============================================
	// INTERNAL USE
	// ============================================
	private String requestAccessToken(String authorizedCode) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", oAuthProperties.getKakao().getClientId());
		params.add("redirect_uri", oAuthProperties.getKakao().getCallbackUrl());
		params.add("code", authorizedCode);

		RestTemplate rt = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
			new HttpEntity<>(params, headers);

		ResponseEntity<String> response = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);

		String tokenJson = response.getBody();
		JSONObject rjson = new JSONObject(tokenJson);

		return rjson.getString("access_token");
	}

	private OAuthUserInfo getUserInfoByToken(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		RestTemplate rt = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

		ResponseEntity<String> response = rt.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			kakaoProfileRequest,
			String.class
		);

		JSONObject body = new JSONObject(response.getBody());
		Long id = body.getLong("id");
		String email = body.getJSONObject("kakao_account").getString("email");
		String nickname = body.getJSONObject("properties").getString("nickname");

		return OAuthUserInfo.of(id.toString(), email, nickname);
	}
}
