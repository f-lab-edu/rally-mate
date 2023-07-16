package com.flab.rallymate.auth.filter;

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

import com.flab.rallymate.auth.CustomUserDetailsService;
import com.flab.rallymate.auth.jwt.JwtAuthenticationFilter;
import com.flab.rallymate.auth.jwt.JwtTokenProvider;
import com.flab.rallymate.auth.jwt.RefreshTokenRedisRepository;
import com.flab.rallymate.auth.model.CustomUserDetails;
import com.flab.rallymate.domain.member.constant.UserRole;

import jakarta.servlet.ServletException;

class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter sut;
    private CustomUserDetails userDetails;
    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;
    private MockFilterChain mockFilterChain;
    private CustomUserDetailsService customUserDetailsService;
	private RefreshTokenRedisRepository refreshTokenRedisRepository;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetails = new CustomUserDetails("nathan@kakao.com", "encryptedPassword", Collections.singletonList(UserRole.ROLE_ADMIN.name()));
        customUserDetailsService = mock(CustomUserDetailsService.class);
		refreshTokenRedisRepository = mock(RefreshTokenRedisRepository.class);
        jwtTokenProvider = new JwtTokenProvider(refreshTokenRedisRepository);
        sut = new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
        mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", getAccessToken(userDetails.getEmail()));
        mockResponse = new MockHttpServletResponse();
        mockFilterChain = new MockFilterChain();
    }

    @Test
    void doFilterInternal_토큰에서_사용자_정보를_가져온다() throws ServletException, IOException {
        when(customUserDetailsService.loadUserByUsername(userDetails.getEmail())).thenReturn(userDetails);


        sut.doFilter(mockRequest, mockResponse, mockFilterChain);


        verify(customUserDetailsService, times(1)).loadUserByUsername(userDetails.getEmail());
    }

    @Test
    void doFilterInternal_토큰에서_가져온_사용자_정보를_SecurityContextHolder에_저장한다() throws ServletException, IOException {
        when(customUserDetailsService.loadUserByUsername(userDetails.getEmail())).thenReturn(userDetails);


        sut.doFilter(mockRequest, mockResponse, mockFilterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();


        assertEquals(userDetails.getEmail(), principal.getUsername());
        assertEquals(userDetails.getRoles(), principal.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()));
        assertEquals(authentication.getAuthorities(), principal.getAuthorities());
    }


    private String getAccessToken(String email) {
        JwtTokenProvider tokenProvider = new JwtTokenProvider(refreshTokenRedisRepository);

		return "Bearer " + tokenProvider.createToken(email, UserRole.ROLE_ADMIN).accessToken();
    }

}
