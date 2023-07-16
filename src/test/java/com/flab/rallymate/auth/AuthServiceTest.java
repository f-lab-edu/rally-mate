package com.flab.rallymate.auth;

import com.flab.rallymate.auth.dto.KakaoAccount;
import com.flab.rallymate.auth.dto.KakaoUserResponseDTO;
import com.flab.rallymate.auth.dto.Properties;
import com.flab.rallymate.auth.jwt.JwtTokenProvider;
import com.flab.rallymate.auth.jwt.dto.JwtTokenDTO;
import com.flab.rallymate.auth.jwt.dto.RefreshTokenEntity;
import com.flab.rallymate.domain.member.constant.UserRole;
import com.flab.rallymate.domain.member.domain.MemberEntity;
import com.flab.rallymate.domain.member.domain.MemberRepository;
import com.flab.rallymate.error.BaseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.flab.rallymate.domain.member.constant.MemberStatus.*;
import static com.flab.rallymate.error.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService sut;
    private KakaoAuthService kakaoAuthService;
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        kakaoAuthService = mock(KakaoAuthService.class);
        memberRepository = mock(MemberRepository.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        sut = new AuthService(
                kakaoAuthService,
                jwtTokenProvider,
                memberRepository
        );
    }

    @Test
    void kakaoLogin_카카오로그인_성공_시에_JWT토큰을_발급한다() {
        String authCode = "sampleAuthCode";
		var sampleTokenDTO = new JwtTokenDTO("sampleAccessToken", "sampleRefreshToken");
        var kakaoResponse = KakaoUserResponseDTO.builder()
                .kakaoAccount(
                        KakaoAccount.builder()
                                .email("sample@sample.com")
                                .build()
                )
                .build();

        when(kakaoAuthService.authenticate(authCode)).thenReturn(kakaoResponse);
        when(memberRepository.findMemberByEmailAndMemberStatus(kakaoResponse.kakaoAccount().email(), ACTIVATE))
				.thenReturn(Optional.of(MemberEntity.builder().id(2L).build()));
		when(jwtTokenProvider.createToken(kakaoResponse.kakaoAccount().email(), UserRole.ROLE_USER)).thenReturn(sampleTokenDTO);


        var loginResult = sut.kakaoLogin(authCode);


        assertEquals(2L, loginResult.memberId());
        assertEquals("sampleAccessToken", loginResult.accessToken());
        assertEquals("sampleRefreshToken", loginResult.refreshToken());
    }

    @Test
    void kakaoLogin_카카오인증_정보가_없을_시_FAILD_KAKAO_AUTH_예외가_발생한다() {

        String authCode = "sampleAuthCode";
        when(kakaoAuthService.authenticate(authCode)).thenReturn(null);


        BaseException baseException = assertThrows(BaseException.class, () -> sut.kakaoLogin(authCode));
        assertEquals(baseException.getMessage(), FAILED_KAKAO_AUTH.getMessage());
    }

    @Test
    void kakaoLogin_카카오_인증정보에_해당하는_멤버가_없을시에_회원가입_후_JWT토큰을_발행한다() {
        String authCode = "sampleAuthCode";
		String samplePassword = "samplePassword";
		var sampleTokenDTO = new JwtTokenDTO("sampleAccessToken", "sampleRefreshToken");
        var kakaoResponse = KakaoUserResponseDTO.builder()
                .kakaoAccount(
                        KakaoAccount.builder()
                                .email("sample@sample.com")
                                .build()
                )
                .properties(
                        Properties.builder()
                                .nickname("sampleUser")
                                .build()
                )
                .build();

        var createMember = MemberEntity.createMember(
                        kakaoResponse.properties().nickname(),
                        kakaoResponse.kakaoAccount().email(),
                        "samplePassword",
                        UserRole.ROLE_USER
        );
        var savedMember = MemberEntity.builder()
                .name(kakaoResponse.properties().nickname())
                .email(kakaoResponse.kakaoAccount().email())
                .password("samplePassword")
                .userRole(UserRole.ROLE_USER)
                .build();

        when(kakaoAuthService.authenticate(authCode)).thenReturn(kakaoResponse);
        when(memberRepository.findMemberByEmailAndMemberStatus(kakaoResponse.kakaoAccount().email(), ACTIVATE)).thenReturn(Optional.empty());
		when(kakaoAuthService.getEncryptedPassword(kakaoResponse.id())).thenReturn(samplePassword);
        when(memberRepository.save(createMember)).thenReturn(savedMember);
		when(jwtTokenProvider.createToken(kakaoResponse.kakaoAccount().email(), UserRole.ROLE_USER)).thenReturn(sampleTokenDTO);


        var loginResponseDTO = sut.kakaoLogin(authCode);


        verify(memberRepository, times(1)).save(createMember);
        assertEquals("sampleAccessToken", loginResponseDTO.accessToken());
        assertEquals("sampleRefreshToken", loginResponseDTO.refreshToken());
    }

	@Test
	void refresh_존재하는_refreshToken_인증요청_시_토큰재발행에_성공한다() {
		String requestRefreshToken = "sampleRefreshToken";
		String email = "sample@sample.com";
		var refreshTokenEntity = RefreshTokenEntity.builder().build();
		var memberEntity = MemberEntity.builder().email("sample@sample.com").build();
		var jwtTokenDTO = JwtTokenDTO.builder()
			.accessToken("sampleAccessToken")
			.refreshToken("sampleRefreshToken")
			.build();

		when(jwtTokenProvider.isTokenExpired(requestRefreshToken)).thenReturn(false);
		when(jwtTokenProvider.getEmailByToken(requestRefreshToken)).thenReturn(email);
		when(jwtTokenProvider.findRefreshTokenBy(email)).thenReturn(Optional.of(refreshTokenEntity));
		when(memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE)).thenReturn(Optional.of(memberEntity));
		when(jwtTokenProvider.createToken(memberEntity.getEmail(), memberEntity.getUserRole())).thenReturn(jwtTokenDTO);


		var result = sut.refresh(requestRefreshToken);


		assertEquals("sampleAccessToken", result.accessToken());
		assertEquals("sampleRefreshToken", result.refreshToken());
	}


	@Test
	void refresh_기간이_지나서_유효하지_않는_refreshToken으로_토큰재발행_시에_INVALID_TOKEN_예외가_발생한다() {
		String requestRefreshToken = "sampleRefreshToken";
		String email = "sample@sample.com";
		var refreshTokenEntity = RefreshTokenEntity.builder().build();
		var memberEntity = MemberEntity.builder().email("sample@sample.com").build();

		when(jwtTokenProvider.isTokenExpired(requestRefreshToken)).thenReturn(true);
		when(jwtTokenProvider.getEmailByToken(requestRefreshToken)).thenReturn(email);
		when(jwtTokenProvider.findRefreshTokenBy(email)).thenReturn(Optional.of(refreshTokenEntity));
		when(memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE)).thenReturn(Optional.of(memberEntity));


		BaseException baseException = assertThrows(BaseException.class, () -> sut.refresh(requestRefreshToken));
		assertEquals(baseException.getMessage(), INVALID_TOKEN.getMessage());
	}

	@Test
	void refresh_존재하지_않는_refreshToken으로_토큰재발행_요청_시에_NOT_FOUND_TOKEN_예외가_발생한다() {
		String requestRefreshToken = "sampleRefreshToken";
		String email = "sample@sample.com";
		var memberEntity = MemberEntity.builder().email("sample@sample.com").build();
		when(jwtTokenProvider.getEmailByToken(requestRefreshToken)).thenReturn(email);
		when(jwtTokenProvider.findRefreshTokenBy(email)).thenReturn(Optional.empty());
		when(memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE)).thenReturn(Optional.of(memberEntity));


		BaseException baseException = assertThrows(BaseException.class, () -> sut.refresh(requestRefreshToken));
		assertEquals(baseException.getMessage(), NOT_FOUND_TOKEN.getMessage());
	}



	@Test
	void refresh_요청한_refreshToken_해당하는_유저정보가_없을시_NOT_FOUND_MEMBER_예외가_발생한다() {
		String requestRefreshToken = "sampleRefreshToken";
		String email = "sample@sample.com";
		var refreshTokenEntity = RefreshTokenEntity.builder().build();
		when(jwtTokenProvider.getEmailByToken(requestRefreshToken)).thenReturn(email);
		when(jwtTokenProvider.findRefreshTokenBy(email)).thenReturn(Optional.of(refreshTokenEntity));
		when(memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE)).thenReturn(Optional.empty());


		BaseException baseException = assertThrows(BaseException.class, () -> sut.refresh(requestRefreshToken));
		assertEquals(baseException.getMessage(), NOT_FOUND_MEMBER.getMessage());
	}



}
