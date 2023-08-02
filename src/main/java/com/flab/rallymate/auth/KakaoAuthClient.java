package com.flab.rallymate.auth;

import com.flab.rallymate.auth.dto.KakaoTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kakaoAuthClient", url = "https://kauth.kakao.com")
public interface KakaoAuthClient {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    KakaoTokenResponseDTO requestToken(
            @RequestBody String kakaoTokenQueryParam
    );

}
