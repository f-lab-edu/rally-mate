package com.flab.rallymate.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record MemberLoginReq(
	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "내용은 필수 입력사항입니다.")
	String email,

	@NotBlank(message = "내용은 필수 입력사항입니다.")
	String password
) {

	@Builder
	public MemberLoginReq {
	}

	public static MemberLoginReq of(String email, String password) {
		return MemberLoginReq.builder()
			.email(email)
			.password(password)
			.build();
	}
}
