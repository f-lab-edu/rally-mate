package com.flab.rallymate.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @ConditionalOnDefaultWebSecurity
// @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	/**
	 * Add Bean PasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 인증 및 인가 설정
	 */
	@Bean
	// @Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/members/**", "/auth/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
				.anyRequest().authenticated()
			)
			.build();
	}
}
