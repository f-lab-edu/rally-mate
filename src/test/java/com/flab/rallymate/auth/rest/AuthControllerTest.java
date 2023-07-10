package com.flab.rallymate.auth.rest;

import com.flab.rallymate.api.AuthController;
import com.flab.rallymate.auth.AuthService;
import com.flab.rallymate.auth.dto.LoginResponseDTO;
import com.flab.rallymate.config.oauth.KakaoOAuthProperties;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.BaseExceptionHandler;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.flab.rallymate.error.ErrorCode.INVALID_TOKEN;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {
	private MockMvc client;
	private AuthService authService;

	private KakaoOAuthProperties properties;

	@BeforeEach
	void setUp() {
		authService = mock(AuthService.class);
		client = MockMvcBuilders.standaloneSetup(new AuthController(authService, properties))
				.setControllerAdvice(new BaseExceptionHandler())
				.build();
	}

	@Test
	void login_로그인에_성공하면_JWT를_리턴한다() throws Exception {
		String sampleAuthCode = "sampleAuthCode";
		when(authService.kakaoLogin(sampleAuthCode)).thenReturn(
			LoginResponseDTO.of(1L, "sampleAccessToken", "sampleRefreshToken"));

		client.perform(get("/auth/login")
				.param("code", sampleAuthCode))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.accessToken", is("sampleAccessToken")))
			.andExpect(jsonPath("$.data.refreshToken", is("sampleRefreshToken")));

		verify(authService).kakaoLogin(sampleAuthCode);
	}

	@Test
	void refresh_유효한_RefreshToken으로_토큰_재발급_요청_시_성공한다() throws Exception {
		String oldValidRefreshToken = "oldRefreshToken";
		when(authService.refresh(oldValidRefreshToken)).thenReturn(new LoginResponseDTO(90L, "90AccessToken", "90RefreshToken"));


		client.perform(post("/auth/refresh")
					.header("RefreshToken", oldValidRefreshToken)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id", is(90)))
				.andExpect(jsonPath("$.data.refreshToken", is("90RefreshToken")))
				.andExpect(jsonPath("$.data.accessToken", is("90AccessToken")));

		verify(authService).refresh(oldValidRefreshToken);
	}

	@Test
	void refresh_유효하지_않는_RefreshToken으로_토큰_재발급_요청_시_401_에러를_반환한다_1번() throws Exception {
		String oldValidRefreshToken = "oldRefreshToken";
		when(authService.refresh(oldValidRefreshToken)).thenReturn(new LoginResponseDTO(90L, "90AccessToken", "90RefreshToken"));


		client.perform(post("/auth/refresh")
					.header("RefreshToken", oldValidRefreshToken)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id", is(90)))
				.andExpect(jsonPath("$.data.refreshToken", is("90RefreshToken")))
				.andExpect(jsonPath("$.data.accessToken", is("90AccessToken")));

		verify(authService).refresh(oldValidRefreshToken);
	}

	@Test
	void refresh_유효하지_않는_RefreshToken으로_토큰_재발급_요청_시_401_에러를_반환한다_3번() throws Exception {
		String oldValidRefreshToken = "oldRefreshToken";
		when(authService.refresh(oldValidRefreshToken)).thenThrow(new BaseException(INVALID_TOKEN));


		client.perform(post("/auth/refresh")
					.header("RefreshToken", oldValidRefreshToken)
				)
				.andExpect(status().isUnauthorized());
	}
}
