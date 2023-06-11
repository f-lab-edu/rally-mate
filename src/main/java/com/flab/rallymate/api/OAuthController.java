package com.flab.rallymate.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flab.rallymate.common.response.BaseHttpResponse;
import com.flab.rallymate.domain.member.dto.MemberLoginRes;
import com.flab.rallymate.domain.oauth.OAuthService;
import com.flab.rallymate.domain.oauth.constant.OAuthType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "oauth", description = "소셜 로그인 API")
@CrossOrigin
@RestController
@RequestMapping(value = "/auth")
public class OAuthController {

	private final OAuthService oAuthService;

	public OAuthController(OAuthService oAuthService) {
		this.oAuthService = oAuthService;
	}

	/**
	 * [53] 사용자 SNS 로그인 요청
	 */
	@GetMapping(value = "/{socialName}")
	@Operation(summary = "소셜 로그인 API 요청")
	public void socialLoginType(@PathVariable(name = "socialName") OAuthType socialLoginType) {
		oAuthService.requestConnect(socialLoginType);
	}

	/**
	 * OAUTH API Server 요청에 의한 callback 처리
	 */
	@GetMapping(value = "/{socialName}/callback")
	@Operation(summary = "소셜 로그인 API Callback 처리")
	public BaseHttpResponse<MemberLoginRes> callback(@PathVariable(name = "socialName") OAuthType socialLoginType,
		@RequestParam(name = "code") String code) throws Exception {
		log.info("### 소셜 로그인 API 서버 응답 code :: {}", code);
		return BaseHttpResponse.success(oAuthService.socialLogin(socialLoginType, code));
	}

}
