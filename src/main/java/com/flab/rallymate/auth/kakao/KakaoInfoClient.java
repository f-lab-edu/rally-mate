package com.flab.rallymate.auth.kakao;

import com.flab.rallymate.auth.domain.dto.KakaoUserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoInfoClient", url = "https://kapi.kakao.com")
public interface KakaoInfoClient {

	@GetMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	KakaoUserResponseDTO getUserInfo(
		@RequestHeader(name = "Authorization")
		String Authorization
	);

}
