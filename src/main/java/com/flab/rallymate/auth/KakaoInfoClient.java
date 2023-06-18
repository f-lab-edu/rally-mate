package com.flab.rallymate.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.flab.rallymate.auth.config.KakaoFeignClientConfig;

@FeignClient(name = "kakaoInfoClient", url = "https://kapi.kakao.com", configuration = KakaoFeignClientConfig.class)
public interface KakaoInfoClient {

	@GetMapping("/v2/user/me")
	ResponseEntity<String> getUserInfo(
		@RequestHeader(name = "Authorization") String Authorization
	);

}
