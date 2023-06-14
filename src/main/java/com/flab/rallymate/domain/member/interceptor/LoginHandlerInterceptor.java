package com.flab.rallymate.domain.member.interceptor;

import com.flab.rallymate.common.response.BaseException;
import com.flab.rallymate.common.response.ErrorCode;
import com.flab.rallymate.common.util.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.flab.rallymate.common.util.jwt.constant.TokenType.X_ACCESS_TOKEN;

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

        if (accessToken != null && tokenProvider.isValidToken(accessToken))
            return true;

        throw new BaseException(ErrorCode.INVALID_TOKEN);
    }
}
