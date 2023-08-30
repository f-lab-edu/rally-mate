package com.flab.rallymate.auth.domain.dto;

import lombok.Builder;

public record KakaoAccountDTO(
	String email
) {

    @Builder
    public KakaoAccountDTO {
    }
}
