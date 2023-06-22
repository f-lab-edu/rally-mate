package com.flab.rallymate.auth.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.flab.rallymate.config.security.CustomUserDetails;
import com.flab.rallymate.config.security.CustomUserDetailsService;
import com.flab.rallymate.domain.member.constant.UserRole;

import jakarta.servlet.ServletException;

class JwtAuthenticationFilterTest {

	private JwtAuthenticationFilter sut;
	private CustomUserDetails userDetails;
	private MockHttpServletRequest mockRequest;
	private MockHttpServletResponse mockResponse;
	private MockFilterChain mockFilterChain;
	private CustomUserDetailsService customUserDetailsService;
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	void setUp() {
		userDetails = new CustomUserDetails("nathan@kakao.com", "encryptedPassword", Collections.singletonList(UserRole.ROLE_ADMIN.name()));
		customUserDetailsService = mock(CustomUserDetailsService.class);
		jwtTokenProvider = mock(JwtTokenProvider.class);
		sut = new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
		mockRequest = new MockHttpServletRequest();
		mockRequest.addHeader("Authorization", getJwtToken(userDetails.getEmail()));
		mockResponse = new MockHttpServletResponse();
		mockFilterChain = new MockFilterChain();
	}

	@Test
	void doFilterInternal_토큰에서_사용자_정보를_가져온다() throws ServletException, IOException {
		when(customUserDetailsService.loadUserByUsername(userDetails.getEmail())).thenReturn(userDetails);
		when(jwtTokenProvider.getEmailByToken(anyString())).thenReturn(userDetails.getEmail());
		when(jwtTokenProvider.isValidToken(anyString())).thenReturn(true);

		sut.doFilter(mockRequest, mockResponse, mockFilterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails)authentication.getPrincipal();

		assertEquals(userDetails.getEmail(), principal.getUsername());
		assertEquals(userDetails.getRoles(),
			principal.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(
				Collectors.toList()));
		assertEquals(authentication.getAuthorities(), principal.getAuthorities());
	}

	private String getJwtToken(String email) {
		JwtTokenProvider tokenProvider = new JwtTokenProvider();
		return tokenProvider.createAccessToken(email, UserRole.ROLE_ADMIN);
	}

}
