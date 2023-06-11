package com.flab.rallymate.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.flab.rallymate.domain.member.interceptor.LoginHandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class LoginInterceptorWebConfiguration implements WebMvcConfigurer {

	private final LoginHandlerInterceptor loginHandlerInterceptor;

	public LoginInterceptorWebConfiguration(LoginHandlerInterceptor loginHandlerInterceptor) {
		this.loginHandlerInterceptor = loginHandlerInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginHandlerInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/auth/**")
			.excludePathPatterns("/members/login", "/members/join")
			.excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html");
	}
}
