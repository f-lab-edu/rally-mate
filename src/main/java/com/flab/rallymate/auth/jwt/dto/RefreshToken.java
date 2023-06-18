package com.flab.rallymate.auth.jwt.dto;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash("refresh")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	private Long id;

	@Indexed
	private String refreshToken;

	@TimeToLive
	private Long expiration;

	@Builder
	public RefreshToken(Long id, String refreshToken, Long expiration) {
		this.id = id;
		this.refreshToken = refreshToken;
		this.expiration = expiration;
	}

	public static RefreshToken of(Long memberId, String refreshToken, Long expiration) {
		return RefreshToken.builder()
			.id(memberId)
			.refreshToken(refreshToken)
			.expiration(expiration / 1_000)
			.build();
	}
}
