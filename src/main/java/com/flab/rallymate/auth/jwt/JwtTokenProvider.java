package com.flab.rallymate.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.flab.rallymate.domain.member.constant.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret-key}")
	private String secretKey = "030605fa79b0bbb71a1cb00f20c160dd93426dc1bbc78881e0e7bbd495a1c0d8b705533c9bd5c1bdae573f5df7489b7b5259f5262ba23a9b59bec17390d3ce81";

	public static final long ACCESS_TOKEN_TIMEOUT = 1000 * 60 * 30L; // 30M
	public static final long REFRESH_TOKEN_TIMEOUT = 1000 * 60 * 60 * 2L;    // 2H
	public static final long REFRESH_TOKEN_REISSUE_TIMEOUT = 1000 * 60 * 60 * 24 * 3L;    // 3Day

	public String createAccessToken(String email, UserRole role) {
		return generateToken(email, role, ACCESS_TOKEN_TIMEOUT);
	}

	public String createRefreshToken(String email, UserRole role) {
		return generateToken(email, role, REFRESH_TOKEN_TIMEOUT);
	}

	private Claims extractClaims(String token) {
		return Jwts
			.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

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

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
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

	public long getRemainMilliSeconds(String token) {
		Date expiration = extractClaims(token).getExpiration();
		Date now = new Date();
		return expiration.getTime() - now.getTime();
	}

	public Long getMemberIdByToken(String token) {
		return extractClaims(token).get("id", Long.class);
	}
}
