package com.flab.rallymate.auth.dto;

import lombok.Builder;

public record KakaoAccount(
	String email
) {

    @Builder
    public KakaoAccount {
    }
}
