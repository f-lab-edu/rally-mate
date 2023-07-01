package com.flab.rallymate.auth;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flab.rallymate.auth.dto.KakaoTokenRequestDTO;
import com.flab.rallymate.auth.dto.KakaoTokenResponseDTO;
import com.flab.rallymate.auth.dto.KakaoUserResponseDTO;
import com.flab.rallymate.auth.dto.KakaoUserResponseDTO.KakaoAccount;
import com.flab.rallymate.auth.dto.KakaoUserResponseDTO.Properties;
import com.flab.rallymate.auth.jwt.JwtTokenProvider;
import com.flab.rallymate.auth.jwt.RefreshTokenRedisRepository;
import com.flab.rallymate.auth.jwt.dto.RefreshToken;
import com.flab.rallymate.config.oauth.KakaoOAuthProperties;
import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.constant.Status;
import com.flab.rallymate.domain.member.constant.UserRole;
import com.flab.rallymate.domain.member.domain.Member;
import com.flab.rallymate.domain.member.dto.MemberJoinRequestDTO;

class AuthServiceTest {
	private AuthService authService;
	private KakaoAuthClient kakaoAuthClient;
	private KakaoInfoClient kakaoInfoClient;

	private KakaoOAuthProperties kakaoOAuthProperties;
	private MemberService memberService;
	private JwtTokenProvider jwtTokenProvider;
	private PasswordEncoder passwordEncoder;
	private RefreshTokenRedisRepository refreshTokenRedisRepository;

	private KakaoTokenResponseDTO kakaoTokenResponseDTO;
	private KakaoUserResponseDTO kakaoUserResponseDTO;
	private Member savedMember;

	@BeforeEach
	void setUp() {
		kakaoAuthClient = mock(KakaoAuthClient.class);
		kakaoInfoClient = mock(KakaoInfoClient.class);
		kakaoOAuthProperties = mock(KakaoOAuthProperties.class);
		memberService = mock(MemberService.class);
		jwtTokenProvider = mock(JwtTokenProvider.class);
		passwordEncoder = mock(PasswordEncoder.class);
		refreshTokenRedisRepository = mock(RefreshTokenRedisRepository.class);
		authService = new AuthService(kakaoAuthClient, kakaoInfoClient, kakaoOAuthProperties, memberService,
			passwordEncoder, jwtTokenProvider, refreshTokenRedisRepository);

		kakaoTokenResponseDTO =  new KakaoTokenResponseDTO("sampleAccessToken", "sampleRefreshToken");
		kakaoUserResponseDTO = new KakaoUserResponseDTO("1", new Properties("nathan"), new KakaoAccount("hj@kakao.com"));
		savedMember =  new Member(1L, "nathan", "hj@kakao.com", "encryptedPassword", Status.USED, UserRole.ROLE_USER, LocalDateTime.now());

	}

	@Test
	void kakaoLogin_넘어온_authCode로_카카오API를_호출한다() throws Exception {
		String sampleAuthCode = "sampleAuthCode";
		RefreshToken refreshToken = new RefreshToken("hj@kakao.com", "sampleRefreshToken", 10L);
		when(kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString()))
			.thenReturn(kakaoTokenResponseDTO);
		when(kakaoInfoClient.getUserInfo("Bearer " + kakaoTokenResponseDTO.accessToken()))
			.thenReturn(kakaoUserResponseDTO);
		when(memberService.findMemberBy(kakaoUserResponseDTO.kakaoAccount().email())).thenReturn(Optional.ofNullable(savedMember));
		when(refreshTokenRedisRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);


		authService.kakaoLogin(sampleAuthCode);


		verify(kakaoAuthClient).requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString());
	}

	@Test
	void kakaoLogin_받아온_accessToken으로_Kakao_사용자정보를_조회한다() throws Exception {
		String sampleAuthCode = "sampleAuthCode";
		RefreshToken refreshToken = new RefreshToken("hj@kakao.com", "sampleRefreshToken", 10L);
		when(kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString()))
			.thenReturn(kakaoTokenResponseDTO);
		when(kakaoInfoClient.getUserInfo("Bearer " + kakaoTokenResponseDTO.accessToken()))
			.thenReturn(kakaoUserResponseDTO);
		when(memberService.findMemberBy(kakaoUserResponseDTO.kakaoAccount().email())).thenReturn(Optional.of(savedMember));
		when(refreshTokenRedisRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);


		authService.kakaoLogin(sampleAuthCode);


		verify(kakaoInfoClient).getUserInfo("Bearer " + kakaoTokenResponseDTO.accessToken());
	}

	@Test
	void kakaoLogin_기존회원인_경우에는_JWT를_발행한다() throws Exception {
		String sampleAuthCode = "sampleAuthCode";
		String jwtAccessToken = "jwtAccessToken";
		RefreshToken refreshToken = new RefreshToken("hj@kakao.com", "sampleRefreshToken", 10L);
		Member findMember = Member.createMember("네이선", "hj@kakao.com", "password", UserRole.ROLE_USER);

		when(kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString()))
			.thenReturn(kakaoTokenResponseDTO);
		when(kakaoInfoClient.getUserInfo("Bearer " + kakaoTokenResponseDTO.accessToken()))
			.thenReturn(kakaoUserResponseDTO);
		when(memberService.findMemberBy(kakaoUserResponseDTO.kakaoAccount().email())).thenReturn(Optional.of(findMember));
		when(jwtTokenProvider.createAccessToken(findMember.getEmail(), findMember.getUserRole())).thenReturn(jwtAccessToken);
		when(refreshTokenRedisRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);


		authService.kakaoLogin(sampleAuthCode);


		verify(memberService).findMemberBy(kakaoUserResponseDTO.kakaoAccount().email());
		verify(jwtTokenProvider).createAccessToken(findMember.getEmail(), findMember.getUserRole());
		verify(jwtTokenProvider).createRefreshToken(findMember.getEmail(), findMember.getUserRole());
	}

	@Test
	void kakaoLogin_기존회원이_아닌_경우에는_회원가입_후_JWT를_발행한다() throws Exception {
		String sampleAuthCode = "sampleAuthCode";
		String jwtAccessToken = "jwtAccessToken";
		RefreshToken refreshToken = new RefreshToken("hj@kakao.com", "sampleRefreshToken", 10L);
		MemberJoinRequestDTO memberJoinReq = MemberJoinRequestDTO.of(
				kakaoUserResponseDTO.properties().nickname(),
				kakaoUserResponseDTO.kakaoAccount().email(),
			"encryptedPassword"
		);
		when(kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, sampleAuthCode).toString()))
			.thenReturn(kakaoTokenResponseDTO);
		when(kakaoInfoClient.getUserInfo("Bearer " + kakaoTokenResponseDTO.accessToken()))
			.thenReturn(kakaoUserResponseDTO);
		when(memberService.findMemberBy(kakaoUserResponseDTO.kakaoAccount().email())).thenReturn(Optional.empty());
		when(memberService.join(memberJoinReq)).thenReturn(savedMember);
		when(jwtTokenProvider.createAccessToken(savedMember.getEmail(), savedMember.getUserRole())).thenReturn(jwtAccessToken);
		when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
		when(refreshTokenRedisRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);


		authService.kakaoLogin(sampleAuthCode);


		verify(memberService).findMemberBy(kakaoUserResponseDTO.kakaoAccount().email());
		verify(memberService).join(memberJoinReq);
		verify(jwtTokenProvider).createAccessToken(savedMember.getEmail(), savedMember.getUserRole());
		verify(jwtTokenProvider).createRefreshToken(savedMember.getEmail(), savedMember.getUserRole());
	}
}
