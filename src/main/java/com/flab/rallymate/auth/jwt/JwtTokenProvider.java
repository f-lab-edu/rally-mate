package com.flab.rallymate.auth.jwt;

import static com.flab.rallymate.auth.jwt.constant.TokenType.*;

import com.flab.rallymate.domain.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenProvider {

    private Key secretKey;
	public static final Long ACCESS_TOKEN_TIMEOUT = 1000 * 60 * 30L; // 30M
	public static final Long REFRESH_TOKEN_TIMEOUT = 1000 * 60 * 60 * 24 * 7L;    // 7Day
	public static final Long REFRESH_TOKEN_REISSUE_TIMEOUT = 1000 * 60 * 60 * 24 * 3L;    // 3Day

	@PostConstruct
	protected void init() {
		secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	}

	public String createAccessToken(Member member) {
		return generateToken(member, ACCESS_TOKEN_TIMEOUT);
	}

	public String createRefreshToken(Member member) {
		return generateToken(member, REFRESH_TOKEN_TIMEOUT);
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

	public String getAccessToken(HttpServletRequest request) { return request.getHeader(X_ACCESS_TOKEN.label); }


    public boolean isValidToken(String token) {
        return (getEmailByToken(token) != null && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

	private String generateToken(Member member, long expireDuration) {
		Claims claims = Jwts.claims();
		claims.put("id", member.getId());

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(member.getEmail())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expireDuration))
			.signWith(getKey())
			.compact();
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
