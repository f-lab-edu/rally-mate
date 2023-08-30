package com.flab.rallymate.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import com.flab.rallymate.auth.jwt.repository.RefreshTokenRedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.flab.rallymate.auth.jwt.dto.JwtTokenDTO;
import com.flab.rallymate.auth.jwt.dto.RefreshToken;
import com.flab.rallymate.member.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Value("${jwt.secret-key}")
	private String secretKey = "030605fa79b0bbb71a1cb00f20c160dd93426dc1bbc78881e0e7bbd495a1c0d8b705533c9bd5c1bdae573f5df7489b7b5259f5262ba23a9b59bec17390d3ce81";

	public static final long ACCESS_TOKEN_TIMEOUT = 1000 * 60 * 30L; // 30M
	public static final long REFRESH_TOKEN_TIMEOUT = 1000 * 60 * 60 * 2L;    // 2H

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractClaims(token);
		return claimsResolver.apply(claims);
	}

	public String getEmailByToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public JwtTokenDTO createToken(String email, UserRole userRole) {
		String accessToken = createAccessToken(email, userRole);
		String refreshToken = createRefreshToken(email, userRole);

		return JwtTokenDTO.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Optional<RefreshToken> findRefreshTokenBy(String email) {
		return refreshTokenRedisRepository.findById(email);
	}

	public void deleteRefreshTokenBy(String email) {
		refreshTokenRedisRepository.deleteById(email);
	}

	private String createAccessToken(String email, UserRole role) {
		return generateToken(email, role, ACCESS_TOKEN_TIMEOUT);
	}

	private String createRefreshToken(String email, UserRole role) {
		String refreshToken = generateToken(email, role, REFRESH_TOKEN_TIMEOUT);

		var refreshTokenEntity = RefreshToken.builder()
			.email(email)
			.expiration(REFRESH_TOKEN_TIMEOUT)
			.build();
		refreshTokenRedisRepository.save(refreshTokenEntity);

		return refreshToken;
	}

	private Claims extractClaims(String token) {
		return Jwts
			.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private String generateToken(String email, UserRole role, long expireDuration) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("roles", role.name());

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expireDuration))
			.signWith(getSigningKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	private Key getSigningKey() {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
