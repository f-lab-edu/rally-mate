package com.flab.rallymate.domain.oauth.social;

import com.flab.rallymate.domain.oauth.constant.OAuthType;
import com.flab.rallymate.domain.oauth.dto.OAuthUserInfo;

public interface SocialOAuth {
	/**
	 * 각 Social Login 페이지로 Redirect 처리할 URL Build
	 * 사용자로부터 로그인 요청을 받아 Social Login Server 인증용 code 요청
	 */
	String getOAuthRedirectUrl();

	/**
	 * API Server로부터 받은 code를 활용하여 사용자 인증 정보 요청
	 * @param authorizedCode API Server 에서 받아온 code
	 * @return API 서버로 부터 응답받은 Json 형태의 결과를 UserInfo 반환
	 */
	OAuthUserInfo getUserInfo(String authorizedCode) throws Exception;

	String getInitPassword();

	default OAuthType type() {
		if (this instanceof KakaoOAuth) {
			return OAuthType.KAKAO;
		} else {
			return null;
		}
	}
}
