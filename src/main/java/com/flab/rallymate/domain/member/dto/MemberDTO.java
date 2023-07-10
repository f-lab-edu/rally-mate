package com.flab.rallymate.domain.member.dto;

import com.flab.rallymate.domain.member.constant.Status;
import com.flab.rallymate.domain.member.domain.Member;

import lombok.Builder;

public record MemberDTO(
	Long id,
	String name,
	String email,
	Status status
) {
	@Builder
	public MemberDTO {
	}

	public static MemberDTO from(Member member) {
		return MemberDTO.builder()
			.id(member.getId())
			.name(member.getName())
			.email(member.getEmail())
			.status(member.getStatus())
			.build();
	}

}
