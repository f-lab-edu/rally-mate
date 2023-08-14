package com.flab.rallymate.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flab.rallymate.auth.dto.KakaoAccount;
import com.flab.rallymate.auth.dto.KakaoTokenRequestDTO;
import com.flab.rallymate.auth.dto.KakaoTokenResponseDTO;
import com.flab.rallymate.auth.dto.KakaoUserResponseDTO;
import com.flab.rallymate.auth.dto.Properties;
import com.flab.rallymate.config.oauth.KakaoOAuthProperties;

class KakaoAuthServiceTest {

	private KakaoAuthService sut;
	private KakaoAuthClient kakaoAuthClient;
	private KakaoInfoClient kakaoInfoClient;
	private KakaoOAuthProperties kakaoOAuthProperties;
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		kakaoAuthClient = mock(KakaoAuthClient.class);
		kakaoInfoClient = mock(KakaoInfoClient.class);
		kakaoOAuthProperties = mock(KakaoOAuthProperties.class);
		passwordEncoder = mock(PasswordEncoder.class);
		sut = new KakaoAuthService(kakaoAuthClient, kakaoInfoClient, kakaoOAuthProperties, passwordEncoder);
	}

	@Test
	void authenticate_카카오인증_성공_시에_올바른_인증정보를_반환한다() {

		String kakaoAccessToken = "sampleKakaoAccessToken";
		var kakaoUserResponseDTO = KakaoUserResponseDTO.builder()
			.id("sampleKakaoId")
			.properties(Properties.builder().nickname("sampleNickname").build())
			.kakaoAccount(KakaoAccount.builder().email("sample@sample.com").build())
			.build();
		when(kakaoInfoClient.getUserInfo("Bearer " + kakaoAccessToken)).thenReturn(kakaoUserResponseDTO);


		var result = sut.authenticate(kakaoAccessToken);


		assertEquals("sampleKakaoId", result.id());
		assertEquals("sampleNickname", result.properties().nickname());
		assertEquals("sample@sample.com", result.kakaoAccount().email());
	}
}
