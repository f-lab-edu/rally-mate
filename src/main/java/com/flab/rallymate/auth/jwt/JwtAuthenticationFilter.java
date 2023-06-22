package com.flab.rallymate.auth.jwt;

import static com.flab.rallymate.error.ErrorCode.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.flab.rallymate.config.security.CustomUserDetailsService;
import com.flab.rallymate.error.BaseException;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Value("${jwt.secret-key}")
	private String secretKey = "030605fa79b0bbb71a1cb00f20c160dd93426dc1bbc78881e0e7bbd495a1c0d8b705533c9bd5c1bdae573f5df7489b7b5259f5262ba23a9b59bec17390d3ce81";

	@Override
	protected void doFilterInternal(@NotBlank HttpServletRequest request,
		@NotBlank HttpServletResponse response,
		@NotBlank FilterChain filterChain) throws ServletException, IOException {

		String accessToken = request.getHeader("Authorization");
		String email = jwtTokenProvider.getEmailByToken(accessToken);

		if (StringUtils.isEmpty(accessToken) || !jwtTokenProvider.isValidToken(accessToken))
			throw new BaseException(INVALID_TOKEN);

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
		authenticateAndSetSecurityContext(request, userDetails);

		filterChain.doFilter(request, response);
	}

	private void authenticateAndSetSecurityContext(HttpServletRequest request, UserDetails userDetails) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

}
