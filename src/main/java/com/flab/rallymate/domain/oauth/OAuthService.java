package com.flab.rallymate.domain.oauth;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.rallymate.domain.member.MemberService;
import com.flab.rallymate.domain.member.dto.MemberDTO;
import com.flab.rallymate.domain.member.dto.MemberJoinReq;
import com.flab.rallymate.domain.member.dto.MemberLoginReq;
import com.flab.rallymate.domain.member.dto.MemberLoginRes;
import com.flab.rallymate.domain.oauth.constant.OAuthType;
import com.flab.rallymate.domain.oauth.dto.OAuthUserInfo;
import com.flab.rallymate.domain.oauth.social.SocialOAuth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OAuthService {
	private final List<SocialOAuth> socialOauthList;
	private final HttpServletResponse response;
	private final MemberService memberService;

	public OAuthService(List<SocialOAuth> socialOauthList, HttpServletResponse response, MemberService memberService) {
		this.socialOauthList = socialOauthList;
		this.response = response;
		this.memberService = memberService;
	}

	public void requestConnect(OAuthType socialType) {
		SocialOAuth socialOAuth = this.findSocialOAuthByType(socialType);
		String redirectURL = socialOAuth.getOAuthRedirectUrl();
		try {
			response.sendRedirect(redirectURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public MemberLoginRes socialLogin(OAuthType socialType, String code) throws Exception {

		// 소셜 정보
		SocialOAuth socialOAuth = this.findSocialOAuthByType(socialType);

		OAuthUserInfo OAuthUserInfo = socialOAuth.getUserInfo(code);

		String socialId = OAuthUserInfo.id();
		String nickname = OAuthUserInfo.nickname();
		String email = OAuthUserInfo.email();

		log.info("socialId = {}, nickname = {}, email = {}", socialId, nickname, email);

		// Case 1. 소셜 이메일 유저 존재
		Optional<MemberDTO> findMember = memberService.findMemberBy(email);
		if (findMember.isEmpty()) {
			// Case 2. 미가입 유저 -> 소셜 회원가입
			MemberJoinReq joinReq = MemberJoinReq.of(nickname, email, socialId + socialOAuth.getInitPassword());
			memberService.join(joinReq);
			log.info("### 회원가입 완료");
		}

		// 회원가입 후 로그인
		MemberLoginReq loginReq = MemberLoginReq.of(email, socialId + socialOAuth.getInitPassword());

		return memberService.login(loginReq);
	}

	// ======================================
	// INTERNAL USE
	// ======================================
	private SocialOAuth findSocialOAuthByType(OAuthType socialLoginType) {
		return socialOauthList.stream()
			.filter(x -> x.type() == socialLoginType)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
	}

}
