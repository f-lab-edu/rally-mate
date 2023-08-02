package com.flab.rallymate.auth.jwt;

import static com.flab.rallymate.error.ErrorCode.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.flab.rallymate.auth.CustomUserDetailsService;
import com.flab.rallymate.error.BaseException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Value("${jwt.secret-key}")
	private String secretKey = "030605fa79b0bbb71a1cb00f20c160dd93426dc1bbc78881e0e7bbd495a1c0d8b705533c9bd5c1bdae573f5df7489b7b5259f5262ba23a9b59bec17390d3ce81";

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.length() > 7) {
			String accessToken = bearerToken.substring(7);
			if (jwtTokenProvider.isTokenExpired(accessToken))
				throw new BaseException(INVALID_TOKEN);

			String email = jwtTokenProvider.getEmailByToken(accessToken);
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

			authenticateAndSetSecurityContext(userDetails);
		}

		filterChain.doFilter(request, response);
	}

	private void authenticateAndSetSecurityContext(UserDetails userDetails) {
		var authentication = new UsernamePasswordAuthenticationToken(
			userDetails,
			null,
			userDetails.getAuthorities()
		);

		var context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

	}

}
