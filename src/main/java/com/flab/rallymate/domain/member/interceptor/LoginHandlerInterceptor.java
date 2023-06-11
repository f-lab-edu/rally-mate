package com.flab.rallymate.domain.member.interceptor;

import static com.flab.rallymate.common.util.jwt.constant.TokenType.*;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.flab.rallymate.common.response.BaseException;
import com.flab.rallymate.common.response.ErrorCode;
import com.flab.rallymate.common.util.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

	private final JwtTokenProvider tokenProvider;

	public LoginHandlerInterceptor(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		String accessToken = request.getHeader(X_ACCESS_TOKEN.name());
		String requestURI = request.getRequestURI();

		if (accessToken != null && tokenProvider.validationToken(accessToken)) {
			log.info("### [LoginHandlerInterceptor] Valid Access Token");
			return true;
		}
		// } else {

			/*
				Refresh Token
			 */

		// HttpSession session = request.getSession();
		// LoginRequest loginRequest = (LoginRequest)session.getAttribute(request.getHeader("X_REFRESH_TOKEN.name()"));

		// if (Objects.isNull(loginRequest))
		// 	throw new BaseException(INVALID_TOKEN);

		// String refreshToken = loginRequest.getRefreshToken();

		// log.info("### [LoginHandlerInterceptor] RefreshToken => {}", refreshToken);

		// if (refreshToken != null && tokenProvider.validationToken(refreshToken)) {
		// 	String newAccessToken = tokenProvider.createAccessToken(loginRequest.getId(),
		// 		loginRequest.getPassword());
		// 	response.setHeader(X_ACCESS_TOKEN.name(), newAccessToken);
		// 	log.info("### [LoginHandlerInterceptor] Renew AccessToken ={} ", newAccessToken);
		// 	return true;
		// }

		log.info("### [LoginHandlerInterceptor] Invalid Token");
		throw new BaseException(ErrorCode.INVALID_TOKEN);
	}
}
