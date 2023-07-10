package com.flab.rallymate.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record KakaoUserResponseDTO(
        String id,
        Properties properties,

        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {

    public record KakaoAccount(
            String email
    ) {
    }

    public record Properties(
            String nickname
    ) {
    }
}



