package com.flab.rallymate.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.flab.rallymate.auth.config.KakaoFeignClientConfig;
import com.flab.rallymate.auth.dto.KakaoTokenResponseDTO;

@FeignClient(name = "kakaoAuthClient", url = "https://kauth.kakao.com", configuration = KakaoFeignClientConfig.class)
public interface KakaoAuthClient {

	@PostMapping(value = "/oauth/token")
	KakaoTokenResponseDTO requestToken(
		@RequestBody String kakaoTokenRequestDTO
	);

}
