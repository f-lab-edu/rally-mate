package com.flab.rallymate.auth.jwt;

import com.flab.rallymate.config.security.CustomUserDetails;
import com.flab.rallymate.domain.member.constant.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtTokenProviderTest {


    private JwtTokenProvider sut;
    //    private String secretKey;
//
//    private byte[] secretKeyBytes;
    private CustomUserDetails userDetails;


    @BeforeEach
    void setUp() {
//        secretKey = "782c178d6cb13d083a5feb4ef5a2bcd1420775e13df0d0cea81d020f0af4c43f";
//        secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        userDetails = new CustomUserDetails("nathan@kakao.com", "samplePassword", Collections.singletonList(UserRole.ROLE_USER.name()));
        sut = new JwtTokenProvider();
    }

    @Test
    void createAccessToken_secretKey로_signing된_accessToken을_생성한다() {
        String accessToken = sut.createAccessToken(userDetails.getEmail(), UserRole.ROLE_USER);


        assertThrows(UnsupportedJwtException.class, () -> {
            Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(accessToken)
                    .getBody()
                    .getSubject();
        });
    }

    @Test
    void createAccessToken_이메일을_사용하여_accessToken을_생성한다() {
        String accessToken = sut.createAccessToken(userDetails.getEmail(), UserRole.ROLE_USER);


        Assertions.assertEquals(userDetails.getEmail(), sut.getEmailByToken(accessToken));
    }

    @Test
    void createAccessToken_만료시간_30분_전인_accessToken을_성생한다() {
        String accessToken = sut.createAccessToken(userDetails.getEmail(), UserRole.ROLE_USER);
        Date issuedAt = sut.extractClaim(accessToken, Claims::getIssuedAt);
        Date expiration = sut.extractClaim(accessToken, Claims::getExpiration);


        Assertions.assertEquals(issuedAt.getTime() + 30 * 60 * 1000L, expiration.getTime());
    }

    @Test
    void createAccessToken_UserRole_가진_accessToken을_생성한다() {
        String accessToken = sut.createAccessToken(userDetails.getEmail(), UserRole.ROLE_USER);


        String roles = sut.extractClaim(accessToken, (claims) -> claims.get("roles", String.class));


        Assertions.assertEquals(UserRole.ROLE_USER.name(), roles);
    }

    @Test
    void createRefreshToken_secretKey로_signing된_refreshToken을_생성한다() {
        String refreshToken = sut.createRefreshToken(userDetails.getEmail(), UserRole.ROLE_USER);


        assertThrows(UnsupportedJwtException.class, () -> {
            Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(refreshToken)
                    .getBody()
                    .getSubject();
        });
    }

    @Test
    void createRefreshToken_이메일을_사용하여_refreshToken을_생성한다() {
        String refreshToken = sut.createRefreshToken(userDetails.getEmail(), UserRole.ROLE_USER);


        Assertions.assertEquals(userDetails.getEmail(), sut.getEmailByToken(refreshToken));
    }

    @Test
    void createRefreshToken_만료시간_2시간_전인_refreshToken을_성생한다() {
        String refreshToken = sut.createRefreshToken(userDetails.getEmail(), UserRole.ROLE_USER);
        Date issuedAt = sut.extractClaim(refreshToken, Claims::getIssuedAt);
        Date expiration = sut.extractClaim(refreshToken, Claims::getExpiration);


        Assertions.assertEquals(issuedAt.getTime() + 1000 * 60 * 60L * 2L, expiration.getTime());
    }

    @Test
    void createRefreshToken_UserRole_가진_refreshToken을_생성한다() {
        String refreshToken = sut.createRefreshToken(userDetails.getEmail(), UserRole.ROLE_USER);


        String roles = sut.extractClaim(refreshToken, (claims) -> claims.get("roles", String.class));


        Assertions.assertEquals(UserRole.ROLE_USER.name(), roles);
    }


}
