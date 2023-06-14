package com.flab.rallymate.common.util.jwt;

import com.flab.rallymate.domain.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

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

    public String createAccessToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put("id", member.getId());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(member.getEmail())
                .setIssuedAt(new Date(ZonedDateTime.now().toInstant().toEpochMilli()))
                .setExpiration(new Date(ZonedDateTime.now().toInstant().toEpochMilli() + accessTokenTimeout))
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getEncoded());
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getEmailByToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public boolean validationToken(String token) {
        return (getEmailByToken(token) != null && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
