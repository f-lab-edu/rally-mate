package com.flab.rallymate.auth.rest;

import com.flab.rallymate.api.AuthController;
import com.flab.rallymate.auth.AuthService;
import com.flab.rallymate.auth.dto.LoginResponseDTO;
import com.flab.rallymate.config.oauth.KakaoOAuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {
	private MockMvc client;
	private AuthService authService;

	private KakaoOAuthProperties properties;

	@BeforeEach
	void setUp() {
		authService = mock(AuthService.class);
		client = MockMvcBuilders.standaloneSetup(new AuthController(authService, properties)).build();
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
	void reIssue_유효한_RefreshToken으로_토큰_재발급_요청_시_성공한다() throws Exception {
		String oldValidRefreshToken = "oldRefreshToken";
		when(authService.reIssue(oldValidRefreshToken)).thenReturn(new LoginResponseDTO(90L, "90AccessToken", "90RefreshToken"));


		client.perform(post("/auth/re-issue")
					.header("RefreshToken", oldValidRefreshToken)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id", is(90)))
				.andExpect(jsonPath("$.data.refreshToken", is("90RefreshToken")))
				.andExpect(jsonPath("$.data.accessToken", is("90AccessToken")));

		verify(authService).reIssue(oldValidRefreshToken);
	}
}
