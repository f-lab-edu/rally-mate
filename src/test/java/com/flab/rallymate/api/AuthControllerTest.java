package com.flab.rallymate.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.flab.rallymate.auth.AuthService;
import com.flab.rallymate.auth.config.KakaoOAuthProperties;
import com.flab.rallymate.auth.dto.LoginResponseDTO;

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
	void 로그인에_성공하면_JWT를_리턴한다() throws Exception {
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
}
