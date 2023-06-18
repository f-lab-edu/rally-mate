package com.flab.rallymate.auth;

import java.util.Optional;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.rallymate.auth.config.KakaoOAuthProperties;
import com.flab.rallymate.auth.dto.KakaoTokenRequestDTO;
import com.flab.rallymate.auth.dto.KakaoTokenResponseDTO;
import com.flab.rallymate.auth.dto.KakaoUserResponseDTO;
import com.flab.rallymate.auth.dto.LoginResponseDTO;
import com.flab.rallymate.auth.jwt.JwtTokenProvider;
import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.domain.Member;
import com.flab.rallymate.domain.member.dto.MemberJoinReq;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final KakaoAuthClient kakaoAuthClient;
	private final KakaoInfoClient kakaoInfoClient;
	private final KakaoOAuthProperties kakaoOAuthProperties;
	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	public LoginResponseDTO kakaoLogin(String authCode) throws JSONException {
		KakaoTokenResponseDTO kakaoTokenResponseDTO = kakaoAuthClient.requestToken(
			KakaoTokenRequestDTO.createKakaoTokenRequestDTO(kakaoOAuthProperties, authCode).toString()
		);

		ResponseEntity<String> kakaoUserResponse = kakaoInfoClient.getUserInfo(
			"Bearer " + kakaoTokenResponseDTO.accessToken());

		JSONObject body = new JSONObject(kakaoUserResponse.getBody());

		KakaoUserResponseDTO kakaoUser = KakaoUserResponseDTO.of(
			body.getLong("id"),
			body.getJSONObject("kakao_account").getString("email"),
			body.getJSONObject("properties").getString("nickname"));

		Optional<Member> findMember = memberService.findMemberBy(kakaoUser.email());

		if (findMember.isEmpty()) {
			MemberJoinReq joinReq = MemberJoinReq.of(
				kakaoUser.nickname(),
				kakaoUser.email(),
				passwordEncoder.encode(kakaoUser.id() + kakaoOAuthProperties.getClientSecret())
			);

			Member savedMember = memberService.join(joinReq);
			String accessToken = jwtTokenProvider.createAccessToken(savedMember);

			return LoginResponseDTO.of(savedMember.getId(), accessToken);
		}
		String accessToken = jwtTokenProvider.createAccessToken(findMember.get());

		return LoginResponseDTO.of(findMember.get().getId(), accessToken);
	}
}
