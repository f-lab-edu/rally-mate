package com.flab.rallymate.api;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flab.rallymate.auth.jwt.JwtTokenProvider;
import com.flab.rallymate.auth.AuthService;
import com.flab.rallymate.auth.KakaoAuthClient;
import com.flab.rallymate.auth.KakaoInfoClient;
import com.flab.rallymate.auth.config.KakaoOAuthProperties;
import com.flab.rallymate.auth.dto.KakaoTokenRequestDTO;
import com.flab.rallymate.auth.dto.KakaoTokenResponseDTO;
import com.flab.rallymate.auth.dto.KakaoUserResponseDTO;
import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.constant.Status;
import com.flab.rallymate.domain.member.domain.Member;
import com.flab.rallymate.domain.member.dto.MemberJoinReq;

class AuthServiceTest {
	private AuthService authService;
	private KakaoAuthClient kakaoAuthClient;
	private KakaoInfoClient kakaoInfoClient;

	private KakaoOAuthProperties kakaoOAuthProperties;
	private MemberService memberService;
	private JwtTokenProvider jwtTokenProvider;
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		kakaoAuthClient = mock(KakaoAuthClient.class);
		kakaoInfoClient = mock(KakaoInfoClient.class);
		kakaoOAuthProperties = mock(KakaoOAuthProperties.class);
		memberService = mock(MemberService.class);
		jwtTokenProvider = mock(JwtTokenProvider.class);
		passwordEncoder = mock(PasswordEncoder.class);
		authService = new AuthService(kakaoAuthClient, kakaoInfoClient, kakaoOAuthProperties, memberService,
			jwtTokenProvider, passwordEncoder);
	}

	@Test
	void kakaoLogin_넘어온_authCode로_카카오API를_호출한다() throws Exception {
		String clientId = "clientId";
		String redirectURL = "redirectURL";

		String sampleAuthCode = "sampleAuthCode";
		KakaoTokenResponseDTO sampleToken = new KakaoTokenResponseDTO("sampleAccesstoken", "sampleRefreshToken");
		KakaoUserResponseDTO kakaoUser = new KakaoUserResponseDTO(1L, "hj@kakao.com", "nathan");
		Member savedMember = new Member(1L, "nathan", "hj@kakao.com", "encryptedPassword", Status.USED,
			LocalDateTime.now());

		when(kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString()))
			.thenReturn(sampleToken);
		when(kakaoInfoClient.getUserInfo("Bearer " + sampleToken.accessToken()))
			.thenReturn(ResponseEntity.ok("{\"id\": 1, \"kakao_account\": {\"email\": \"hj@kakao.com\"}, \"properties\": {\"nickname\": \"nathan\"}}"));
		when(memberService.findMemberBy(kakaoUser.email())).thenReturn(Optional.of(savedMember));

		authService.kakaoLogin(sampleAuthCode);

		verify(kakaoAuthClient).requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString());
	}

	@Test
	void kakaoLogin_받아온_accessToken으로_Kakao_사용자정보를_조회한다() throws Exception {
		String clientId = "clientId";
		String redirectURL = "redirectURL";
		String sampleAuthCode = "sampleAuthCode";
		KakaoUserResponseDTO kakaoUser = new KakaoUserResponseDTO(1L, "hj@kakao.com", "nathan");
		KakaoTokenResponseDTO sampleToken = new KakaoTokenResponseDTO("sampleAccesstoken", "sampleRefreshToken");
		Member savedMember = new Member(1L, "nathan", "hj@kakao.com", "encryptedPassword", Status.USED,
			LocalDateTime.now());

		when(kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString()))
			.thenReturn(sampleToken);
		when(kakaoInfoClient.getUserInfo("Bearer " + sampleToken.accessToken()))
			.thenReturn(ResponseEntity.ok("{\"id\": 1, \"kakao_account\": {\"email\": \"hj@kakao.com\"}, \"properties\": {\"nickname\": \"nathan\"}}"));
		when(memberService.findMemberBy(kakaoUser.email())).thenReturn(Optional.of(savedMember));

		authService.kakaoLogin(sampleAuthCode);

		verify(kakaoInfoClient).getUserInfo("Bearer " + sampleToken.accessToken());
	}

	@Test
	void kakaoLogin_기존회원인_경우에는_JWT를_발행한다() throws Exception {
		String clientId = "clientId";
		String redirectURL = "redirectURL";
		String sampleAuthCode = "sampleAuthCode";
		KakaoTokenResponseDTO sampleToken = new KakaoTokenResponseDTO("sampleAccesstoken", "sampleRefreshToken");
		String jwtAccessToken = "jwtAccessToken";

		KakaoUserResponseDTO kakaoUser = new KakaoUserResponseDTO(1L, "hj@kakao.com", "nathan");
		Member findMember = Member.createMember("네이선", "hj@kakao.com", "password");

		when(kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString()))
			.thenReturn(sampleToken);
		when(kakaoInfoClient.getUserInfo("Bearer " + sampleToken.accessToken()))
			.thenReturn(ResponseEntity.ok("{\"id\": 1, \"kakao_account\": {\"email\": \"hj@kakao.com\"}, \"properties\": {\"nickname\": \"nathan\"}}"));
		when(memberService.findMemberBy(kakaoUser.email())).thenReturn(Optional.of(findMember));
		when(jwtTokenProvider.createAccessToken(findMember)).thenReturn(jwtAccessToken);

		authService.kakaoLogin(sampleAuthCode);

		verify(memberService).findMemberBy(kakaoUser.email());
		verify(jwtTokenProvider).createAccessToken(findMember);
	}

	@Test
	void kakaoLogin_기존회원이_아닌_경우에는_회원가입_후_JWT를_발행한다() throws Exception {
		String clientId = "clientId";
		String redirectURL = "redirectURL";
		String sampleAuthCode = "sampleAuthCode";
		KakaoTokenResponseDTO sampleToken = new KakaoTokenResponseDTO("sampleAccesstoken", "sampleRefreshToken");
		String jwtAccessToken = "jwtAccessToken";

		KakaoUserResponseDTO kakaoUser = new KakaoUserResponseDTO(1L, "hj@kakao.com", "nathan");
		MemberJoinReq memberJoinReq = MemberJoinReq.of(kakaoUser.nickname(), kakaoUser.email(), "encryptedPassword");
		Member savedMember = Member.createMember("nathan", "hj@kakao.com", "encryptedPassword");

		when(kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString()))
			.thenReturn(sampleToken);
		when(kakaoInfoClient.getUserInfo("Bearer " + sampleToken.accessToken()))
			.thenReturn(ResponseEntity.ok("{\"id\": 1, \"kakao_account\": {\"email\": \"hj@kakao.com\"}, \"properties\": {\"nickname\": \"nathan\"}}"));
		when(memberService.findMemberBy(kakaoUser.email())).thenReturn(Optional.empty());
		when(memberService.join(memberJoinReq)).thenReturn(savedMember);
		when(jwtTokenProvider.createAccessToken(savedMember)).thenReturn(jwtAccessToken);
		when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");

		authService.kakaoLogin(sampleAuthCode);

		verify(memberService).findMemberBy(kakaoUser.email());
		verify(memberService).join(memberJoinReq);
		verify(jwtTokenProvider).createAccessToken(savedMember);
	}
}
