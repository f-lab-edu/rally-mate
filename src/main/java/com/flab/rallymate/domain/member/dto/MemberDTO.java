package com.flab.rallymate.domain.member.dto;

import com.flab.rallymate.domain.member.constant.MemberStatus;
import com.flab.rallymate.domain.member.domain.MemberEntity;

import lombok.Builder;

public record MemberDTO(
	Long id,
	String name,
	String email,
	MemberStatus status
) {
	@Builder
	public MemberDTO {
	}
}
