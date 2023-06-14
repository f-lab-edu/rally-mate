package com.flab.rallymate.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KakaoOAuthResponse {

    private String accessToken;
    private String expiresIn;
    private String tokenType;
    private String scope;
    private String idToken;
    private String refreshToken;

}

