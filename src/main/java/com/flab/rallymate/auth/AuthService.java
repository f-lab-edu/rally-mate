package com.flab.rallymate.auth;

import com.flab.rallymate.auth.domain.dto.LoginResponseDTO;
import com.flab.rallymate.auth.jwt.JwtTokenProvider;
import com.flab.rallymate.auth.jwt.dto.JwtTokenDTO;
import com.flab.rallymate.auth.kakao.KakaoAuthService;
import com.flab.rallymate.member.enums.MemberStatus;
import com.flab.rallymate.member.enums.UserRole;
import com.flab.rallymate.member.domain.MemberEntity;
import com.flab.rallymate.member.repository.MemberRepository;
import com.flab.rallymate.error.BaseException;
import com.flab.rallymate.error.ErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoAuthService kakaoAuthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public LoginResponseDTO kakaoLogin(String kakaoAccessToken) {
        var kakaoResponse = kakaoAuthService.authenticate(kakaoAccessToken);

        if (kakaoResponse == null) {
            throw new BaseException(ErrorCode.FAILED_KAKAO_AUTH);
        }

        var email = kakaoResponse.kakaoAccountDTO().email();
        var findMember = memberRepository.findMemberByEmailAndMemberStatus(email, MemberStatus.ACTIVATE);

        if (findMember.isEmpty()) {
            var createMember = MemberEntity.builder()
                    .name(kakaoResponse.kakaoUserPropertiesDTO().nickname())
                    .email(kakaoResponse.kakaoAccountDTO().email())
                    .password(kakaoAuthService.getEncryptedPassword(kakaoResponse.id()))
                    .career(0)
                    .userRole(UserRole.ROLE_USER)
                    .build();

            var savedMember = memberRepository.save(createMember);

			return createLoginResponse(savedMember, UserRole.ROLE_USER);
        }

		return createLoginResponse(findMember.get(), findMember.get().getUserRole());
    }

    public JwtTokenDTO refresh(String refreshToken) {

		String email = jwtTokenProvider.getEmailByToken(refreshToken);

		if (jwtTokenProvider.isTokenExpired(refreshToken)) {
			throw new BaseException(ErrorCode.INVALID_TOKEN);
		}

		var memberEntity = memberRepository.findMemberByEmailAndMemberStatus(email, MemberStatus.ACTIVATE)
			.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_MEMBER));

		var refreshTokenEntity = jwtTokenProvider.findRefreshTokenBy(email)
			.orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_TOKEN));
		jwtTokenProvider.deleteRefreshTokenBy(refreshTokenEntity.getEmail());

		return jwtTokenProvider.createToken(memberEntity.getEmail(), memberEntity.getUserRole());
    }

   private LoginResponseDTO createLoginResponse(MemberEntity memberEntity, UserRole userRole) {
	   var jwtTokenDTO = jwtTokenProvider.createToken(memberEntity.getEmail(), userRole);

	   return LoginResponseDTO.builder()
		   .memberId(memberEntity.getId())
		   .accessToken(jwtTokenDTO.accessToken())
		   .refreshToken(jwtTokenDTO.refreshToken())
		   .build();
   }
}
