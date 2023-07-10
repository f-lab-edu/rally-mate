package com.flab.rallymate.auth;

import com.flab.rallymate.auth.dto.KakaoTokenRequestDTO;
import com.flab.rallymate.auth.dto.LoginResponseDTO;
import com.flab.rallymate.auth.jwt.JwtTokenProvider;
import com.flab.rallymate.auth.jwt.RefreshTokenRedisRepository;
import com.flab.rallymate.auth.jwt.dto.RefreshToken;
import com.flab.rallymate.config.oauth.KakaoOAuthProperties;
import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.domain.Member;
import com.flab.rallymate.domain.member.dto.MemberJoinRequestDTO;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.flab.rallymate.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoInfoClient kakaoInfoClient;
    private final KakaoOAuthProperties kakaoOAuthProperties;

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public LoginResponseDTO kakaoLogin(String authCode) {
        var kakaoTokenResponse = kakaoAuthClient.requestToken(
                KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, authCode).toString()
        );

        var kakaoUserResponse = kakaoInfoClient.getUserInfo(
                "Bearer " + kakaoTokenResponse.accessToken());

        if (kakaoUserResponse.id() == null)
            throw new BaseException(ErrorCode.NOT_FOUND_MEMBER);

        Optional<Member> findMember = memberService.findMemberBy(kakaoUserResponse.kakaoAccount().email());

        if (findMember.isEmpty()) {
            MemberJoinRequestDTO joinReq = MemberJoinRequestDTO.of(
				kakaoUserResponse.properties().nickname(),
				kakaoUserResponse.kakaoAccount().email(),
				passwordEncoder.encode(kakaoUserResponse.id() + kakaoOAuthProperties.getClientSecret())
            );

            Member savedMember = memberService.join(joinReq);
            return createLoginResponse(savedMember);
        }

        return createLoginResponse(findMember.get());
    }

    public LoginResponseDTO refresh(String refreshToken) {

        String email = jwtTokenProvider.getEmailByToken(refreshToken);
        String findRefreshToken = refreshTokenRedisRepository.findById(email)
                                .orElseThrow(() -> new BaseException(NOT_FOUND_TOKEN)).getRefreshToken();

        if (!findRefreshToken.equals(refreshToken))
            throw new BaseException(INVALID_TOKEN);

        var findMember = memberService.findMemberBy(email).orElseThrow(() -> new BaseException(NOT_FOUND_MEMBER));
        return createLoginResponse(findMember);
    }

    private LoginResponseDTO createLoginResponse(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getUserRole());
        RefreshToken refreshToken = saveRefreshToken(member);

        return LoginResponseDTO.of(member.getId(), accessToken, refreshToken.getRefreshToken());
    }

    private RefreshToken saveRefreshToken(Member member) {
        return refreshTokenRedisRepository.save(RefreshToken.of(member.getEmail(),
                jwtTokenProvider.createRefreshToken(member.getEmail(), member.getUserRole()),
                JwtTokenProvider.REFRESH_TOKEN_TIMEOUT));
    }
}
