package com.flab.rallymate.auth.dto;

import lombok.Builder;

public record Properties(
	String nickname
) {
	@Builder
	public Properties {}
}
