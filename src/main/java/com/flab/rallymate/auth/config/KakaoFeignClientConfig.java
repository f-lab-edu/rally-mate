package com.flab.rallymate.auth.config;

import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.RequestInterceptor;

public class KakaoFeignClientConfig {

	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> template.header("Content-Type", "application/x-www-form-urlencoded");
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
