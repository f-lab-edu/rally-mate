package com.flab.rallymate.common.util.jwt;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.flab.rallymate.domain.member.domain.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private Key secretKey;
	private static final Long accessTokenTimeout = 1000 * 60 * 60 * 2L;        // 2H
	private static final Long refreshTokenTimeout = 1000 * 60 * 60 * 24 * 7L;    // 7H

	@PostConstruct
	protected void init() {
		secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	/**
	 * 토큰을 생성한다.
	 *
	 * @param member 회원 엔티티
	 * @return JWT 토큰
	 */
	public String createAccessToken(Member member) {
		Claims claims = Jwts.claims();
		claims.put("id", member.getId());
		//        claims.put("role", JwtRole.MEMBER);
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(member.getEmail())
			.setIssuedAt(new Date(ZonedDateTime.now().toInstant().toEpochMilli()))
			.setExpiration(new Date(ZonedDateTime.now().toInstant().toEpochMilli() + accessTokenTimeout))
			.signWith(getKey())
			.compact();
	}

	/**
	 * 키를 가져온다.
	 *
	 * @return key
	 */
	private Key getKey() {
		return Keys.hmacShaKeyFor(secretKey.getEncoded());
	}

	/**
	 * 토큰에서 모든 정보 추출
	 *
	 * @param token JWT 토큰
	 * @return claims 토큰 정보
	 */
	private Claims extractAllClaims(String token) {
		return Jwts
			.parserBuilder()
			.setSigningKey(getKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	/**
	 * 주어진 토큰에서 클레임 값을 추출하는 메서드
	 *
	 * @param token          추출할 클레임이 포함된 JWT 토큰
	 * @param claimsResolver 클레임 값을 추출하기 위한 함수형 인터페이스
	 * @param <T>            반환할 클레임 값의 타입
	 * @return 주어진 토큰에서 추출된 클레임 값
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * 토큰 사용자 이메일 추출
	 *
	 * @param token JWT 토큰
	 * @return string 이메일
	 */
	public String getEmailByToken(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * 유요한 토큰인지 확인
	 *
	 * @param token JWT 토큰
	 * @return boolean   토큰 유효 여부
	 */
	public boolean validationToken(String token) {
		return (getEmailByToken(token) != null && !isTokenExpired(token));
	}

	/**
	 * 토큰 만료 여부 확인
	 *
	 * @param token JWT 토큰
	 * @return boolean   토큰 만료 여부
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * 토큰 만료 일자 추출
	 *
	 * @param token JWT 토큰
	 * @return date 토큰 만료 일자
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}
