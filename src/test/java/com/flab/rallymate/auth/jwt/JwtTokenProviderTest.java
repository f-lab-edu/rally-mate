package com.flab.rallymate.auth.jwt;

import com.flab.rallymate.auth.domain.model.CustomUserDetails;
import com.flab.rallymate.auth.jwt.repository.RefreshTokenRedisRepository;
import com.flab.rallymate.member.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class JwtTokenProviderTest {


    private JwtTokenProvider sut;
	private RefreshTokenRedisRepository refreshTokenRedisRepository;
    private CustomUserDetails userDetails;


    @BeforeEach
    void setUp() {
        userDetails = new CustomUserDetails("nathan@kakao.com", "samplePassword", Collections.singletonList(UserRole.ROLE_USER.name()));
		refreshTokenRedisRepository = mock(RefreshTokenRedisRepository.class);
        sut = new JwtTokenProvider(refreshTokenRedisRepository);
    }

    @Test
    void createAccessToken_secretKey로_signing된_accessToken을_생성한다() {
		var jwtTokenDTO = sut.createToken(userDetails.getEmail(), UserRole.ROLE_USER);


        assertThrows(UnsupportedJwtException.class, () -> {
            Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(jwtTokenDTO.accessToken())
                    .getBody()
                    .getSubject();
        });
    }

    @Test
    void createAccessToken_이메일을_사용하여_accessToken을_생성한다() {
		var jwtTokenDTO = sut.createToken(userDetails.getEmail(), UserRole.ROLE_USER);


        Assertions.assertEquals(userDetails.getEmail(), sut.getEmailByToken(jwtTokenDTO.accessToken()));
    }

    @Test
    void createAccessToken_만료시간_30분_전인_accessToken을_성생한다() {
		var jwtTokenDTO = sut.createToken(userDetails.getEmail(), UserRole.ROLE_USER);
        Date issuedAt = sut.extractClaim(jwtTokenDTO.accessToken(), Claims::getIssuedAt);
        Date expiration = sut.extractClaim(jwtTokenDTO.accessToken(), Claims::getExpiration);


        Assertions.assertEquals(issuedAt.getTime() + 30 * 60 * 1000L, expiration.getTime());
    }

    @Test
    void createAccessToken_UserRole_가진_accessToken을_생성한다() {
		var jwtTokenDTO = sut.createToken(userDetails.getEmail(), UserRole.ROLE_USER);


        String roles = sut.extractClaim(jwtTokenDTO.accessToken(), (claims) -> claims.get("roles", String.class));


        Assertions.assertEquals(UserRole.ROLE_USER.name(), roles);
    }

    @Test
    void createRefreshToken_secretKey로_signing된_refreshToken을_생성한다() {
		var jwtTokenDTO = sut.createToken(userDetails.getEmail(), UserRole.ROLE_USER);

        assertThrows(UnsupportedJwtException.class, () -> {
            Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(jwtTokenDTO.refreshToken())
                    .getBody()
                    .getSubject();
        });
    }

    @Test
    void createRefreshToken_이메일을_사용하여_refreshToken을_생성한다() {
		var jwtTokenDTO = sut.createToken(userDetails.getEmail(), UserRole.ROLE_USER);


        Assertions.assertEquals(userDetails.getEmail(), sut.getEmailByToken(jwtTokenDTO.refreshToken()));
    }

    @Test
    void createRefreshToken_만료시간_2시간_전인_refreshToken을_성생한다() {
		var jwtTokenDTO = sut.createToken(userDetails.getEmail(), UserRole.ROLE_USER);
        Date issuedAt = sut.extractClaim(jwtTokenDTO.refreshToken(), Claims::getIssuedAt);
        Date expiration = sut.extractClaim(jwtTokenDTO.refreshToken(), Claims::getExpiration);


        Assertions.assertEquals(issuedAt.getTime() + 1000 * 60 * 60L * 2L, expiration.getTime());
    }

    @Test
    void createRefreshToken_UserRole_가진_refreshToken을_생성한다() {
		var jwtTokenDTO = sut.createToken(userDetails.getEmail(), UserRole.ROLE_USER);


        String roles = sut.extractClaim(jwtTokenDTO.refreshToken(), (claims) -> claims.get("roles", String.class));


        Assertions.assertEquals(UserRole.ROLE_USER.name(), roles);
    }


}
