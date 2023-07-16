package com.flab.rallymate.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flab.rallymate.auth.dto.KakaoTokenRequestDTO;
import com.flab.rallymate.auth.dto.KakaoUserResponseDTO;
import com.flab.rallymate.config.oauth.KakaoOAuthProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoInfoClient kakaoInfoClient;
    private final KakaoOAuthProperties kakaoOAuthProperties;
	private final PasswordEncoder passwordEncoder;

    public KakaoUserResponseDTO authenticate(String authCode) {
		var kakaoTokenResponseDTO = kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, authCode).toString()
		);
		var kakaoUserInfo = kakaoInfoClient.getUserInfo("Bearer " + kakaoTokenResponseDTO.accessToken());

		return KakaoUserResponseDTO.builder()
			.id(kakaoUserInfo.id())
			.properties(kakaoUserInfo.properties())
			.kakaoAccount(kakaoUserInfo.kakaoAccount())
			.build();
	}

	public String getEncryptedPassword(String kakaoId) {
		return passwordEncoder.encode(kakaoId + kakaoOAuthProperties.getClientSecret());
	}
}
