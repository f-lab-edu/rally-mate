package com.flab.rallymate.auth;

import static com.flab.rallymate.error.ErrorCode.*;
import static com.flab.rallymate.member.enums.MemberStatus.ACTIVATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.flab.rallymate.auth.kakao.KakaoAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flab.rallymate.auth.domain.dto.KakaoAccountDTO;
import com.flab.rallymate.auth.domain.dto.KakaoUserResponseDTO;
import com.flab.rallymate.auth.domain.dto.KakaoUserPropertiesDTO;
import com.flab.rallymate.auth.jwt.JwtTokenProvider;
import com.flab.rallymate.auth.jwt.dto.JwtTokenDTO;
import com.flab.rallymate.auth.jwt.dto.RefreshToken;
import com.flab.rallymate.member.enums.UserRole;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.member.repository.MemberRepository;
import com.flab.rallymate.error.BaseException;

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
                .kakaoAccountDTO(
                        KakaoAccountDTO.builder()
                                .email("sample@sample.com")
                                .build()
                )
                .build();
		var memberEntity = Optional.of(MemberEntity.builder()
			.id(2L)
			.email("sample@sample.com")
			.userRole(UserRole.ROLE_USER)
			.build()
		);

        when(kakaoAuthService.authenticate(authCode)).thenReturn(kakaoResponse);
        when(memberRepository.findMemberByEmailAndMemberStatus(kakaoResponse.kakaoAccountDTO().email(), ACTIVATE))
				.thenReturn(memberEntity);
		when(jwtTokenProvider.createToken(kakaoResponse.kakaoAccountDTO().email(), UserRole.ROLE_USER)).thenReturn(sampleTokenDTO);


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
                .kakaoAccountDTO(
                        KakaoAccountDTO.builder()
                                .email("sample@sample.com")
                                .build()
                )
                .kakaoUserPropertiesDTO(
                        KakaoUserPropertiesDTO.builder()
                                .nickname("sampleUser")
                                .build()
                )
                .build();

		var createMember = MemberEntity.builder()
				.name(kakaoResponse.kakaoUserPropertiesDTO().nickname())
				.email(kakaoResponse.kakaoAccountDTO().email())
				.password("samplePassword")
				.career(0)
				.userRole(UserRole.ROLE_USER)
				.build();

        var savedMember = MemberEntity.builder()
                .name(kakaoResponse.kakaoUserPropertiesDTO().nickname())
                .email(kakaoResponse.kakaoAccountDTO().email())
                .password("samplePassword")
				.career(createMember.getCareer())
                .userRole(UserRole.ROLE_USER)
                .build();

        when(kakaoAuthService.authenticate(authCode)).thenReturn(kakaoResponse);
        when(memberRepository.findMemberByEmailAndMemberStatus(kakaoResponse.kakaoAccountDTO().email(), ACTIVATE)).thenReturn(Optional.empty());
		when(kakaoAuthService.getEncryptedPassword(kakaoResponse.id())).thenReturn(samplePassword);
        when(memberRepository.save(createMember)).thenReturn(savedMember);
		when(jwtTokenProvider.createToken(kakaoResponse.kakaoAccountDTO().email(), UserRole.ROLE_USER)).thenReturn(sampleTokenDTO);


        var loginResponseDTO = sut.kakaoLogin(authCode);


        verify(memberRepository, times(1)).save(createMember);
        assertEquals("sampleAccessToken", loginResponseDTO.accessToken());
        assertEquals("sampleRefreshToken", loginResponseDTO.refreshToken());
    }

	@Test
	void refresh_존재하는_refreshToken_인증요청_시_토큰재발행에_성공한다() {
		String requestRefreshToken = "sampleRefreshToken";
		String email = "sample@sample.com";
		var refreshTokenEntity = RefreshToken.builder().build();
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
		var refreshTokenEntity = RefreshToken.builder().build();
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
		var refreshTokenEntity = RefreshToken.builder().build();
		when(jwtTokenProvider.getEmailByToken(requestRefreshToken)).thenReturn(email);
		when(jwtTokenProvider.findRefreshTokenBy(email)).thenReturn(Optional.of(refreshTokenEntity));
		when(memberRepository.findMemberByEmailAndMemberStatus(email, ACTIVATE)).thenReturn(Optional.empty());


		BaseException baseException = assertThrows(BaseException.class, () -> sut.refresh(requestRefreshToken));
		assertEquals(baseException.getMessage(), NOT_FOUND_MEMBER.getMessage());
	}



}
